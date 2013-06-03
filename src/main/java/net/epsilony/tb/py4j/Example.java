/* (c) Copyright by Man YUAN */
package net.epsilony.tb.py4j;

import py4j.GatewayServer;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class Example {

    public void doSomething() {
        System.out.println("this = " + this);
    }

    public static void main(String[] args) {
        GatewayServer server = new GatewayServer(new Example());
        server.start();
    }
}
