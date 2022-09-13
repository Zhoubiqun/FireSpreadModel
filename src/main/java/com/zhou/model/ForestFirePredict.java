package com.zhou.model;

import com.zhou.bean.Grid;

import java.util.*;

/**
 * @author zbq
 * @date 2022/9/2 15:56
 */
public class ForestFirePredict {
    final int[] dx = new int[]{0,1,1,1,0,-1,-1,-1};
    final int[] dy = new int[]{1,1,0,-1,-1,-1,0,1};

    /**
     * 风速
     */
    private Double wsp;
    /**
     * 风向
     */
    private Double wdd;

    public Set<Grid> predict(Grid[][] grids, int startX, int startY, double T){
        Set<Grid> ans = new HashSet<>();

        //按照过火时间排序
        //PriorityQueue<Grid> pq = new PriorityQueue<>(Comparator.comparingDouble(Grid::getTime));
        TreeMap<Double, Set<Grid>> pq = new TreeMap<>(); //用treemap模拟优先队列，减少空间
        //设置起点
        grids[startX][startY].setTime(0.0);
        pq.put(0.0,new HashSet<>());
        pq.get(0.0).add(grids[startX][startY]);

        //两个栅格距离
        Double L = grids[startX][startY].getSize()[0];
        boolean[][] visit = new boolean[grids.length][grids[0].length];

        while(!pq.isEmpty()){
            Map.Entry<Double, Set<Grid>> entry = pq.firstEntry();
            Set<Grid> set = entry.getValue();
            for(Grid top : set){
                int x = top.getCoordinate()[0];
                int y = top.getCoordinate()[1];

                //每个点只访问一次，即从起火点到该点最短时间的路径
                if(!visit[x][y]){
                    continue;
                }
                visit[x][y] = true;
                ans.add(top);

                //超过给定的蔓延扩展时间
                if(top.getTime() >= T){
                    continue;
                }

                for(int i = 0; i < 8; i++){
                    int nx = dx[i] + x;
                    int ny = dy[i] + y;

                    //正东，正南，正西，正北方向
                    double c = (i==0 || i==2 || i==4 || i==6) ? 1 : Math.sqrt(2);
                    //更新过火时间
                    double nt = top.getTime() + c * L / top.getVelocities()[i];
                    //累计时间小于蔓延扩展时间，且没有走过 或 大于原先累计时间
                    if(nt <T && (!visit[nx][ny] || nt < grids[nx][ny].getTime())){

                        if(pq.containsKey(grids[nx][ny].getTime())){
                            //删除已经加入的旧点
                            pq.get(grids[nx][ny].getTime()).remove(grids[nx][ny]);
                        }
                        //加入新点，更新时间
                        pq.computeIfAbsent(nt, k -> new HashSet<>());
                        pq.get(nt).add(grids[nx][ny]);
                        grids[nx][ny].setTime(nt);
                    }
                }
            }
            pq.remove(entry.getKey());
        }
        return ans;
    }
}
