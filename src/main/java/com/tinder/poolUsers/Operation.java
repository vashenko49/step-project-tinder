package com.tinder.poolUsers;

import java.io.IOException;

@FunctionalInterface
public interface Operation {
    void operation() throws IOException;
}
