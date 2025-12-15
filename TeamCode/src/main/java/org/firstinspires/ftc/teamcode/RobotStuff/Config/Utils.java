package org.firstinspires.ftc.teamcode.RobotStuff.Config;

public class Utils {
    public static final String PRIORITY_PRIORITY = "aaa";
    public static final String PRIORITY = "aba";
    public static final String UTILITY = "aca";
    public static final String WORKING = "acb";
    public static final String TUNE = "acc";
    public static final String TESTING = "baa";
    public static final String UNUSABLE_DUETOHARDWARE = "caa";
    public static final String UNUSABLE_DUETOCODE = "cba";
    public static final String BORKEN = "zzz";

    public enum ArtifactTypes {
        GREEN,
        PURPLE,
        NONE
    }

    public static final Utils.ArtifactTypes[] GPPGPP = new Utils.ArtifactTypes[]{
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE
    };

    public static final Utils.ArtifactTypes[] PGPPGP = new Utils.ArtifactTypes[]{
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE
    };

    public static final Utils.ArtifactTypes[] PPGPPG = new Utils.ArtifactTypes[]{
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN
    };
}