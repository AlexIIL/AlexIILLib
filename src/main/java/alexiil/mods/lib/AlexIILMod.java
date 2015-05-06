package alexiil.mods.lib;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.Logger;

import alexiil.mods.lib.git.Commit;
import alexiil.mods.lib.git.GitHubUser;
import alexiil.mods.lib.git.Release;
import alexiil.mods.lib.git.SiteRequester;
import alexiil.mods.lib.net.INetworkProvider;

public abstract class AlexIILMod {
    public Property netRate, netDistance, connectExternally;
    public Logger log;
    public ModMetadata meta;
    public CreativeTabs tab;
    public ConfigAccess cfg;
    public INetworkProvider provider;

    private List<GitHubUser> contributors = null;
    private List<Commit> commits = null;
    private List<Release> releases = null;
    private Commit thisCommit = null;

    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();
        meta = event.getModMetadata();
        cfg = new ConfigAccess(event.getSuggestedConfigurationFile(), this);

        netRate = cfg.getProp("netRate", 120);
        netRate.comment = "How long to wait between sending updates, in ticks";

        netDistance = cfg.getProp("netDistance", 120);
        netDistance.comment = "The range in which to send updates to players";

        connectExternally = cfg.getProp("connectExternally", true);
        connectExternally.comment =
            "If this is enabled, it will connect to an external server (drone.io) to fetch changelogs and release information";
    }

    public void initSiteVersioning() {
        String droneSite = getBaseSite();
        contributors = Collections.unmodifiableList(SiteRequester.getContributors(droneSite + "contributors.json"));
        if (contributors.size() == 0)
            meta.authorList.add("Could not connect to GitHub to fetch the rest...");
        for (GitHubUser c : contributors) {
            if ("AlexIIL".equals(c.login))
                continue;
            meta.authorList.add(c.login);
        }

        commits = SiteRequester.getCommits(droneSite + "commits.json");
        Collections.sort(commits, new Comparator<Commit>() {
            @Override
            public int compare(Commit c0, Commit c1) {
                return c1.commit.committer.date.compareTo(c0.commit.committer.date);
            }
        });
        commits = Collections.unmodifiableList(commits);

        for (Commit c : commits)
            if (getCommitHash().equals(c.sha))
                thisCommit = c;
        if (thisCommit == null && commits.size() > 0 && getBuildType() == 2) {
            log.warn("Didn't find my commit! This is unexpected, consider this a bug!");
            log.warn("Commit Hash : \"" + getCommitHash() + "\"");
        }

        releases = Collections.unmodifiableList(SiteRequester.getReleases(droneSite + "releases.json"));
    }

    public void postInit(FMLPostInitializationEvent event) {
        cfg.saveAll();
    }

    public String format(String toFormat, Object... objects) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            return I18n.format(toFormat, objects);
        return toFormat;
    }

    public List<GitHubUser> getContributors() {
        if (contributors == null)
            initSiteVersioning();
        return contributors;
    }

    public List<Commit> getCommits() {
        if (contributors == null)
            initSiteVersioning();
        return commits;
    }

    public Commit getCurrentCommit() {
        if (contributors == null)
            initSiteVersioning();
        return thisCommit;
    }

    public List<Release> getReleases() {
        if (contributors == null)
            initSiteVersioning();
        return releases;
    }

    /** @return The commit hash that this build is. You can ignore this method if you don't use my VersionInfo for
     *         building you mod */
    public abstract String getCommitHash();

    /** @return The build type to show what kind of build this is. 0 is a local (in development environment), 1 is manual
     *         build, 2 is an auto-build. You can ignore all the other methods todo with versioning if you don't return
     *         2 */
    public abstract int getBuildType();

    /** The base site to get version information from. This should have 3 files, named "contributors.json",
     * "commits.json" and "releases.json". You can return null if you return anything other than 2 in
     * {@link #getBuildType()} */
    public String getBaseSite() {
        return "https://drone.io/github.com/" + getUser() + "/" + getRepo() + "/files/VersionInfo/build/libs/version/";
    }

    /** The user of the github repo that you develop in. You can return null if you override {@link #getBaseSite()} or if
     * you return anything other than 2 in {@link #getBuildType()} */
    public abstract String getUser();

    /** The repository name of the github repo that you develop in. You can return null if you override
     * {@link #getBaseSite()} or if you return anything other than 2 in {@link #getBuildType()} */
    public abstract String getRepo();
}
