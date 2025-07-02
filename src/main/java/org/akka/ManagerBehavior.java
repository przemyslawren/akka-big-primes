package org.akka;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagerBehavior extends AbstractBehavior<String> {
    private static final Logger log = LoggerFactory.getLogger(ManagerBehavior.class);

    private ManagerBehavior(ActorContext<String> context) {
        super(context);
    }

    public static Behavior<String> create() {
        return Behaviors.setup(ManagerBehavior::new);
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("start", () -> {
                    for (int i = 0; i < 20; i++) {
                        String workerName = "worker_" + i;
                        ActorRef<String> workerActor = getContext().spawn(WorkerBehavior.create(), workerName);
                        workerActor.tell("start");
                    }
                    return this;
                })
                .onAnyMessage((message) -> {
                    log.error("WRONG MESSAGE BRO");
                    return this;
                })
                .build();
    }
}
