package org.akka;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.SortedSet;
import java.util.TreeSet;

public class ManagerBehavior extends AbstractBehavior<ManagerBehavior.Command> {
    private static final Logger log = LoggerFactory.getLogger(ManagerBehavior.class);

    public interface Command extends Serializable {}

    public static class InstructionCommand implements Command {
        @Serial
        private static final long serialVersionUID = 1L;
        private final String message;

        public InstructionCommand(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class ResultCommand implements Command {
        @Serial
        private static final long serialVersionUID = 1L;
        private final BigInteger prime;

        public BigInteger getPrime() {
            return prime;
        }

        public ResultCommand(BigInteger prime) {
            this.prime = prime;
        }
    }

    private ManagerBehavior(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(ManagerBehavior::new);
    }

    private SortedSet<BigInteger> primes = new TreeSet<>();

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(InstructionCommand.class, (command) -> {
                    if (command.getMessage().equals("start")) {
                        for (int i = 0; i < 20; i++) {
                            String workerName = "worker_" + i;
                            ActorRef<WorkerBehavior.Command> workerActor = getContext().spawn(WorkerBehavior.create(), workerName);
                            workerActor.tell(new WorkerBehavior.Command("start", getContext().getSelf()));
                        }
                    }
                    return this;
                }).onMessage(ResultCommand.class, (command) -> {
                    primes.add(command.getPrime());
                    System.out.println("I have received " + primes.size() + " prime numbers");
                    if (primes.size() == 20) {
                        primes.forEach(System.out::println);
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
