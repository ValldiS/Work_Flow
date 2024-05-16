package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Work_2 {

    public static void main(String[] args) throws InterruptedException, Exception {

        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(texts.length);
        List<Callable<Integer>> taskCallable = new ArrayList<>();


        for (String text : texts) {
            Callable<Integer> callable = () -> {
                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                return maxSize;
            };
            taskCallable.add(callable);
        }

        List<Future<Integer>> answer = executorService.invokeAll(taskCallable);

        executorService.shutdown();

        int maxSize = 0;
        for (int i = 0; i < answer.size(); i++) {
            if (maxSize < answer.get(i).get()) {
                maxSize = answer.get(i).get();
            }
        }
        System.out.println("Максимальный интервал значений " + maxSize);


    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
