/*
 * Copyright 2017 Datalogics, Inc.
 */

package com.datalogics.pdf.samples.util;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class FileUtils {
    /**
     * Search from a root, looking for PDF files, and return all their paths.
     *
     * @param root the root path of the search
     * @return an Observable that signals every PDF file found in the path
     */
    public static Observable<Path> pdfFiles(final Path root) {
        final File file = root.toFile();
        if (!file.exists()) {
            return Observable.empty();
        }

        if (file.isFile()) {
            return Observable.just(root);
        }

        if (!file.isDirectory()) {
            return Observable.error(new RuntimeException(root + " is not a file or directory"));
        }

        // TODO this overload of Observable.create is deprecated; find out what the replacement is.
        return Observable.create(new Observable.OnSubscribe<Path>() {
            @Override
            public void call(final Subscriber<? super Path> subscriber) {
                try {
                    Files.walkFileTree(root, new FileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                                        throws IOException {
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                                        throws IOException {
                            if (attrs.isRegularFile() && file.toString().toLowerCase().endsWith(".pdf")) {
                                subscriber.onNext(file);
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path file, IOException exc)
                                        throws IOException {
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                                        throws IOException {
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    /**
     * Walk all the paths indicated for each argument, searching recursively for PDF files, and print them out.
     *
     * @param args program arguments
     */
    public static void main(final String[] args) {
        Observable.from(args)
                  .flatMap(new Func1<String, Observable<Path>>() {
                      @Override
                      public Observable<Path> call(String arg) {
                          return pdfFiles(Paths.get(arg));
                      }
                  })
                  .subscribe(new Action1<Path>() {
                      @Override
                      public void call(Path path) {
                          System.out.println(path);
                      }
                  });
    }
}
