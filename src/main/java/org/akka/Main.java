package org.akka;

import akka.actor.typed.ActorSystem;

public class Main {
//    public static void main(String[] args) {
//        ActorSystem<String> actorSystem = ActorSystem.create(FirstSimpleBehavior.create(), "FirstActorSystem");
//        actorSystem.tell("say hello");
//        actorSystem.tell("who are you");
//        actorSystem.tell("create a child");
//        actorSystem.tell("Random message");
//    }

    public static void main(String[] args) {
        ActorSystem<ManagerBehavior.Command> actorSystem =
                ActorSystem.create(ManagerBehavior.create(), "ProbablePrimeSystem");

        actorSystem.tell(new ManagerBehavior.InstructionCommand("start"));
    }
}