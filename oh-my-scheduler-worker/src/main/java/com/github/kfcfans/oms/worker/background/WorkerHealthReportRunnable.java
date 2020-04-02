package com.github.kfcfans.oms.worker.background;

import akka.actor.ActorSelection;
import com.github.kfcfans.common.RemoteConstant;
import com.github.kfcfans.common.model.SystemMetrics;
import com.github.kfcfans.common.request.WorkerHeartbeat;
import com.github.kfcfans.oms.worker.OhMyWorker;
import com.github.kfcfans.oms.worker.common.utils.AkkaUtils;
import com.github.kfcfans.oms.worker.common.utils.SystemInfoUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Worker健康度定时上报
 *
 * @author tjq
 * @since 2020/3/25
 */
@Slf4j
@AllArgsConstructor
public class WorkerHealthReportRunnable implements Runnable {

    @Override
    public void run() {

        SystemMetrics systemMetrics = SystemInfoUtils.getSystemMetrics();

        WorkerHeartbeat heartbeat = new WorkerHeartbeat();
        heartbeat.setSystemMetrics(systemMetrics);
        heartbeat.setWorkerAddress(OhMyWorker.getWorkerAddress());
        heartbeat.setAppName(OhMyWorker.getConfig().getAppName());

        // 发送请求
        String serverPath = AkkaUtils.getAkkaServerNodePath(RemoteConstant.SERVER_ACTOR_NAME);
        ActorSelection actorSelection = OhMyWorker.actorSystem.actorSelection(serverPath);
        actorSelection.tell(heartbeat, null);
    }
}
