package com.nlstn.jarvis.module.modules.job.events;

import com.nlstn.jarvis.events.JarvisEvent;
import com.nlstn.jarvis.module.modules.job.Job;

public abstract class JobEvent extends JarvisEvent {

    private Job job;

    public JobEvent(String name, Job job) {
        super(name);
        this.job = job;
    }

    public Job getJob() {
        return job;
    }

}