package group23.pacman.system;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Antonio Zaitoun on 04/01/2019.
 */
public class AssetManager {

    private static final String DEFAULT_DIR_NAME = "support-files";

    private static File RESOURCE_DIR = new File(new File(".").getAbsolutePath() + File.separator + DEFAULT_DIR_NAME);

    private static void setup_dirs() {
        RESOURCE_DIR.mkdirs();
        System.out.println("RES DIR SET TO " + RESOURCE_DIR.getAbsolutePath());
    }

    static {
        setup_dirs();
    }

    public static void setDir(File dir){
        RESOURCE_DIR = dir;
        setup_dirs();
    }

    /**
     * This method receives a list of classes which are defined as assets protocols.
     * The method then iterates over the list and executes the protocol by creating an instance.
     *
     * @param assets
     * @throws MissingAssetException
     */
    public static void init(Class<? extends AssetProtocol>... assets) throws MissingAssetException {
        for (Class<? extends AssetProtocol> asset : assets) {
            String assetName = "Unknown";
            try {
                Constructor<?> ctor = asset.getConstructor();
                AssetProtocol object = (AssetProtocol) ctor.newInstance();
                assetName = object.name();

                //run some checks on object
                boolean res = object.checkIfExist(RESOURCE_DIR);

                if (!res) {
                    object.createDefaultFiles(RESOURCE_DIR);
                }

                System.out.println("Successfully Loaded " + assetName);

            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new MissingAssetException("Failed to load asset named \"" + assetName + "\"");
            }
        }
    }

    /**
     * Missing Asset Exception
     */
    public static class MissingAssetException extends RuntimeException {
        public MissingAssetException(String s) { super(s); }
    }

    public static String file(String name) {
        return RESOURCE_DIR.getAbsolutePath() + File.separator + name;
    }
}
