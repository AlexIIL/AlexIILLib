package alexiil.mods.lib.coremod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.common.config.Property;
import alexiil.mods.lib.AlexIILLib;
import alexiil.mods.lib.ErrorHandling;

public class RoamingIPAddress {
    private static Map<String, String> nameToIP = Collections.synchronizedMap(new HashMap<String, String>());

    public static Property roamingIPLoc = null;

    public static void init() {
        roamingIPLoc = AlexIILLib.instance.cfg.getProp("roamingIP.location", "null");
        if (AlexIILLib.roamingIP.getBoolean())
            loadIPAddresses();
    }

    /** If its in the form roaming-[name]:port then its a roaming server */
    public static ServerData getModifiedRoamingServerData(ServerData data) {
        if (!AlexIILLib.roamingIP.getBoolean())
            return data;
        if (!data.serverIP.startsWith("roaming-"))
            return data;
        int indexHyphen = data.serverIP.indexOf("-");
        if (indexHyphen == -1)
            return data;
        int indexColon = data.serverIP.indexOf(":");
        String port;
        String name;
        if (indexColon == -1) {
            port = "25565";
            name = data.serverIP.substring(indexHyphen + 1);
        }
        else {
            port = data.serverIP.substring(indexColon + 1);
            name = data.serverIP.substring(indexHyphen + 1, indexColon);
        }
        String ip;
        if (nameToIP.containsKey(name.intern()))
            ip = nameToIP.get(name.intern());
        else
            ip = "invalid";
        ip += ":" + port;
        ServerData newData = new ServerData(name, ip);
        newData.gameVersion = data.gameVersion;
        newData.version = data.version;
        return newData;
    }

    public static void loadIPAddresses() {
        try {
            File ipFile = new File(roamingIPLoc.getString());
            if (ipFile.exists()) {
                BufferedReader br = null;
                String line = "";
                try {
                    br = new BufferedReader(new FileReader(ipFile));
                    line = br.readLine();
                    int lineNo = -1;
                    while (line != null) {
                        lineNo++;
                        if (line.length() == 0)
                            AlexIILLib.instance.log.warn("Found an empty line at line #" + lineNo);
                        else if (!line.contains("="))
                            AlexIILLib.instance.log.warn("Did not find an equals sign at line #" + lineNo + ", \"" + line + "\"");
                        else if (line.indexOf("=") == 0)
                            AlexIILLib.instance.log.warn("Found an equals sign at the start of the line #" + lineNo + ", \"" + line + "\"");
                        else if (line.indexOf("=") == line.length() - 1)
                            AlexIILLib.instance.log.warn("Found an equals sign at the start of the line #" + lineNo + ", \"" + line + "\"");
                        else if (line.indexOf("=") != line.lastIndexOf("="))
                            AlexIILLib.instance.log.warn("Found more than one equals sign in the line #" + lineNo + ", \"" + line + "\"");
                        else {
                            String[] ip = line.split("=");
                            AlexIILLib.instance.log.info("Added (" + ip[0] + ") -> (" + ip[1] + ")");
                            nameToIP.put(ip[0], ip[1]);
                        }
                        line = br.readLine();
                    }
                }
                catch (Throwable t) {
                    ErrorHandling.printStackTrace(t, "loading the line " + line);
                    // AlexIILLib.instance.log.warn("loading IP addresses failed (" + t.getMessage() + ")");
                    // AlexIILLib.instance.log.warn(t.getClass());
                }
                finally {
                    if (br != null)
                        br.close();
                }
            }
            else
                AlexIILLib.instance.log.warn("IP file did not exist!");
        }
        catch (Throwable t) {
            // AlexIILLib.instance.log.warn("Could not open IP address file (" + t.getMessage() + ")");
            ErrorHandling.printStackTrace(t, "opening the IP address file");
            // AlexIILLib.instance.log.warn(t.getClass() + " @ " + t.getStackTrace()[0].getLineNumber());
        }
    }
}
