package org.indexcache;

import java.io.Closeable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ReadWriteCollection {
    public ScopeLock read() {
        readLock.lock();
        return readLock;
    }

    public ScopeLock write() {
        writeLock.lock();
        return writeLock;
    }

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private ScopeLock readLock = new ScopeLock(lock.readLock());
    private ScopeLock writeLock = new ScopeLock(lock.writeLock());

    public class ScopeLock implements Closeable {
        private Lock lock;

        public ScopeLock(Lock lock) {
            this.lock = lock;
        }

        public ScopeLock lock() {
            lock.lock();
            return this;
        }

        @Override
        public void close() {
            lock.unlock();
        }
    }
}
