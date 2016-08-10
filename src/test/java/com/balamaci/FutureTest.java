package com.balamaci;


import java.util.concurrent.CompletableFuture;

/**
 * @author Serban Balamaci
 */
public class FutureTest {

//    @Test
    public void testComposition() {
        CompletableFuture<String> workflowUppercase = CompletableFuture.supplyAsync(() -> {
            System.out.println("Before work");
            sleep(5000);
            return "Hello";
        }).thenApply((str) -> {
            sleep(1000);
            String upper = str.toUpperCase();
            System.out.println("Done " + upper);
            return upper;
        });

        CompletableFuture<Integer> workflowCredit = CompletableFuture.supplyAsync(() -> {
            System.out.println("Before power");
            if(true) {
                throw new RuntimeException("Error completing");
            }
            sleep(7000);
            return 9;
        }).exceptionally((th) -> -1);

        workflowUppercase
                .thenCombine(workflowCredit, (str, nr) -> str + nr).handle((str, th) -> {
            if (th != null) {
                System.out.println("Encountered exception " + th);
            }
            System.out.println("Got " + str);
            return str;
        });

        while(true) {
            System.out.println("Alive");
            sleep(1000);
        }
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
