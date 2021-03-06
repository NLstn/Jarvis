package com.jarvis.module.modules.job;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jarvis.commands.Command;
import com.jarvis.module.ModuleHandler;
import com.jarvis.module.modules.command.events.CommandEvent;
import com.jarvis.module.modules.command.events.CommandEventHandler;
import com.jarvis.module.modules.command.events.CommandFailedEvent;
import com.jarvis.module.modules.command.events.CommandFinishedEvent;
import com.jarvis.module.modules.command.events.CommandStartedEvent;
import com.jarvis.module.modules.job.events.JobCreatedEvent;
import com.jarvis.module.modules.job.events.JobEvent;
import com.jarvis.module.modules.job.events.JobEventHandler;
import com.jarvis.module.modules.job.events.JobFailedEvent;
import com.jarvis.module.modules.job.events.JobFinishedEvent;
import com.jarvis.module.modules.job.events.JobStartedEvent;
import com.jarvis.module.modules.logging.Logger;

public abstract class Job implements CommandEventHandler {

    private String id;
    private String name;
    private JobExecutionPlan executionPlan;
    private JobStatus status;
    private Command command;

    private List<JobEventHandler> eventHandlers;

    public Job(String name, JobExecutionPlan executionPlan, Command command) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.executionPlan = executionPlan;
        this.status = JobStatus.INITIAL;
        this.command = command;
        command.addEventHandler(this);
        this.eventHandlers = new ArrayList<>();
    }

    public void execute() {
        status = JobStatus.RUNNING;
        Logger.getRootLogger().trace("Submitting job " + id);
        ModuleHandler.getWorkerModule().submitRunnable(command);
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return true if the job reached the state, where it should be executed (e.g.
     *         start time reached)
     */
    public boolean isReady() {
        return false;
    }

    public JobExecutionPlan getExecutionPlan() {
        return executionPlan;
    }

    public String getName() {
        return name;
    }

    public JobStatus getStatus() {
        return status;
    }

    protected void setStatus(JobStatus status) {
        switch (status) {
            case FAILED:
                raiseEvent(new JobFailedEvent(this));
                raiseEvent(new JobFinishedEvent(this));
                break;
            case FINISHED:
                raiseEvent(new JobFinishedEvent(this));
                break;
            case INITIAL:
                raiseEvent(new JobCreatedEvent(this));
                break;
            case PLANNED:
                break;
            case RUNNING:
                raiseEvent(new JobStartedEvent(this));
                break;
            default:
                break;
        }
        this.status = status;
    }

    protected void raiseEvent(JobEvent e) {
        for (JobEventHandler handler : eventHandlers)
            handler.handleEvent(e);
    }

    public void handleEvent(CommandEvent e) {
        if (e instanceof CommandStartedEvent)
            setStatus(JobStatus.RUNNING);
        else if (e instanceof CommandFailedEvent)
            setStatus(JobStatus.FAILED);
        else if (e instanceof CommandFinishedEvent)
            setStatus(JobStatus.FINISHED);

    }
}