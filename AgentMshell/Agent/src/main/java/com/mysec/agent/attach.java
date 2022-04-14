package com.mysec.agent;
import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class attach {
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        //获取当前系统上运行的jvm虚拟机列表
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for(VirtualMachineDescriptor vmd : list){
            System.out.println("PID: " + vmd.id() + "\t; Name: "+vmd.displayName());
        }
        Scanner scan = new Scanner(System.in);
        System.out.print("目标进程PID：");
        String pid = scan.nextLine();
        VirtualMachine vm = VirtualMachine.attach(pid);
        System.out.println("Agent attached!");
        System.out.print("请输入Agent路径：");
        String agentPath = scan.nextLine();
        vm.loadAgent(agentPath);
        vm.detach();
    }
}
