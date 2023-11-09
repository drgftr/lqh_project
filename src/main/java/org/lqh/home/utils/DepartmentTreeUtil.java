package org.lqh.home.utils;

import org.lqh.home.entity.Department;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/29
 **/
public class DepartmentTreeUtil {
    /**
     * list 转 tree
     *
     * @param nodes
     * @return
     */
    public static List<Department> listToTree(List<Department> nodes) {
        Map<Long, List<Department>> nodeMap = nodes.stream().filter(node -> node.getParentId() != 0)
                .collect(Collectors.groupingBy(node -> node.getParent().getId()));

        nodes.forEach(node -> node.setChildren(nodeMap.get(node.getId())));

        List<Department> treeNode = nodes.stream().filter(node -> node.getParentId() == 0).collect(Collectors.toList());
        return treeNode;
    }
}
