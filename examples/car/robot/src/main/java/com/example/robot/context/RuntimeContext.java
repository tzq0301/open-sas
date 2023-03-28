package com.example.robot.context;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface RuntimeContext {
    @NonNull
    RuntimeContextInformation getRuntimeContextInformation();
}
