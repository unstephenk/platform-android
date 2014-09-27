package com.ushahidi.android;

import com.ushahidi.android.module.UshahidiModule;

import android.app.Application;

import java.util.List;

import dagger.ObjectGraph;
import timber.log.Timber;


public class UshahidiApplication extends Application {

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
        initializeDependencyInjector();
    }

    /**
     * Extend the dependency container graph with new dependencies provided by the modules passed as
     * arguments.
     *
     * @param modules used to populate the dependency container.
     */
    public ObjectGraph add(List<Object> modules) {
        if (modules == null) {
            throw new IllegalArgumentException(
                    "You can't add a null module, review your getModules() implementation");
        }
        return mObjectGraph.plus(modules.toArray());
    }

    private void initializeDependencyInjector() {
        mObjectGraph = ObjectGraph.create(new UshahidiModule(this));
        mObjectGraph.inject(this);
        mObjectGraph.injectStatics();
    }

    /**
     * Inject the supplied {@code object} using the activity-specific graph.
     */
    public void inject(Object object) {
        mObjectGraph.inject(object);
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.HollowTree {

        @Override
        public void i(String message, Object... args) {
            // TODO Setup Crashlytics.log(String.format(message, args));
        }

        @Override
        public void i(Throwable t, String message, Object... args) {
            i(message, args); // Just add to the log.
        }

        @Override
        public void e(String message, Object... args) {
            i("ERROR: " + message, args); // Just add to the log.
        }

        @Override
        public void e(Throwable t, String message, Object... args) {
            e(message, args);

            // TODO Setup Crashlytics.logException(t);
        }
    }
}
