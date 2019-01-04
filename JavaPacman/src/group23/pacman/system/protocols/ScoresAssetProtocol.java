package group23.pacman.system.protocols;

import group23.pacman.system.AssetProtocol;

import java.io.File;

/**
 * Created by Antonio Zaitoun on 04/01/2019.
 */
public class ScoresAssetProtocol implements AssetProtocol{
    @Override
    public String name() {
        return "Scores";
    }

    @Override
    public boolean checkIfExist(File directory) {
        return false;
    }

    @Override
    public void createDefaultFiles(File directory) {

    }
}
