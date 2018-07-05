package com.example.demo;

import scala.Serializable;
import scala.Tuple2;

public class TupleComparator implements java.util.Comparator<Tuple2<Integer, String>>, Serializable {
    @Override
    public int compare(Tuple2<Integer, String> tuple1, Tuple2<Integer, String> tuple2) {
        return Integer.compare(tuple1._1, tuple2._1);
    }
}
