package com.example.jmx;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Random;

public class GameBeanTest {

    public static void main(String[] args) throws Exception {

        // Create MBean instance
        Game game = new Game();

        // Register MBean
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("com.example.jmx:name=application");
        server.registerMBean(game, objectName);

        // Keep program running & increment counter periodically.
        while (true) {
            Random random = new Random();
            int ran = random.nextInt(11);
            if (ran%11 == 0) {
                game.playFootball("Real Madrid");
            } else if (ran%11 == 1) {
                game.setPlayerName("De Gea");
            } else if (ran%11 == 7) {
                game.setPlayerName("Christiano");
            } else if (ran%11 == 10) {
                game.setPlayerName("Messi");
            }
            Thread.sleep(1000);
        }
    }
}
