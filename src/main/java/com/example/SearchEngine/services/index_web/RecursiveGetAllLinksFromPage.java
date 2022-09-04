package com.example.SearchEngine.services.index_web;

import com.example.SearchEngine.models.index_web.NodePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

@Component
public class RecursiveGetAllLinksFromPage extends RecursiveAction {
    private NodePage nodePage;
    private NodeService nodeService;

    @Autowired
    public RecursiveGetAllLinksFromPage(NodePage nodePage, NodeService nodeService) {
        this.nodePage = nodePage;
        this.nodeService = nodeService;
    }

    @Override
    protected void compute() {

        List<RecursiveGetAllLinksFromPage> taskList = new ArrayList<>();

        nodePage.setPath(nodePage.getPrefix() + nodePage.getSuffix());


        nodeService.handlerPage(nodePage);


        for (String refOnChild : nodePage.getRefOnChilds()) {
            NodePage nodePageChild = new NodePage();
            nodePageChild.setPrefix(nodePage.getPrefix());
            nodePageChild.setSuffix(refOnChild);
            nodePageChild.setTimeBetweenRequest(nodePage.getTimeBetweenRequest());
            nodePageChild.setSiteId(nodePage.getSiteId());

            RecursiveGetAllLinksFromPage task = new RecursiveGetAllLinksFromPage(nodePageChild, nodeService);
            try {
                Thread.sleep(nodePage.getTimeBetweenRequest());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            task.fork();
            taskList.add(task);
        }
        taskList.stream().forEach(t -> t.join());
    }




}
