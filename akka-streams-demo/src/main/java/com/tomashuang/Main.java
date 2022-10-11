package com.tomashuang;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.ThrottleMode;
import akka.stream.javadsl.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.FiniteDuration;

import java.math.BigInteger;
import java.util.concurrent.CompletionStage;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] argv) {
        final ActorSystem system = ActorSystem.create("QuickStart");
        final Source<Integer, NotUsed> source = Source.range(1, 10);
        final Source<BigInteger, NotUsed> factorials =
                source.scan(BigInteger.ONE, (acc, next) -> acc.multiply(BigInteger.valueOf(next)));
        final CompletionStage<Done> result = factorials
                .zipWith(Source.range(0, 99), (num, idx) -> String.format("%d! = %s", idx, num))
                .throttle(1, FiniteDuration.apply(1,"s"),1, ThrottleMode.shaping())
                .runForeach(System.out::println, ActorMaterializer.create(system));
        result.whenComplete((d,e) -> logger.info(d.toString()));
        result.thenRun(system::terminate);
    }
}
