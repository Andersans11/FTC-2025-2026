package org.firstinspires.ftc.teamcode.RobotStuff.PedroJSON.main;

import com.pedropathing.pathgen.PathChain;

public class PathChainDirectory {

    PathChain pathChain;
    int id;
    public PathChainDirectory(PathChain pathChain, int id) {
        this.pathChain  = pathChain;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public PathChain getPathChain() {
        return pathChain;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPathChain(PathChain pathChain) {
        this.pathChain = pathChain;
    }
}
