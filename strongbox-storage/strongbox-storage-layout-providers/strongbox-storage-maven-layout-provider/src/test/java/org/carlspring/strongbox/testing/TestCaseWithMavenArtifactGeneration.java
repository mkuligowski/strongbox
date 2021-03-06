package org.carlspring.strongbox.testing;

import org.carlspring.strongbox.artifact.MavenArtifact;
import org.carlspring.strongbox.artifact.MavenArtifactUtils;
import org.carlspring.strongbox.artifact.MavenDetachedArtifact;
import org.carlspring.strongbox.artifact.generator.MavenArtifactGenerator;
import org.carlspring.strongbox.providers.layout.LayoutProvider;
import org.carlspring.strongbox.providers.layout.LayoutProviderRegistry;
import org.carlspring.strongbox.repository.MavenRepositoryFeatures;
import org.carlspring.strongbox.repository.RepositoryManagementStrategy;
import org.carlspring.strongbox.resource.ConfigurationResourceResolver;
import org.carlspring.strongbox.storage.Storage;
import org.carlspring.strongbox.storage.repository.Repository;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * @author mtodorov
 */
public class TestCaseWithMavenArtifactGeneration
        extends TestCaseWithRepositoryManagement
{

    @Inject
    LayoutProviderRegistry layoutProviderRegistry;

    @Inject
    MavenRepositoryFeatures features;

    public MavenArtifact generateArtifact(String basedir, String gavtc)
            throws IOException,
                   XmlPullParserException,
                   NoSuchAlgorithmException
    {
        return generateArtifact(Paths.get(basedir), gavtc);
    }

    public MavenArtifact generateArtifact(Path basedir, String gavtc)
            throws IOException,
                   XmlPullParserException,
                   NoSuchAlgorithmException
    {
        MavenArtifact artifact = MavenArtifactUtils.getArtifactFromGAVTC(gavtc);
        artifact.setPath(basedir.resolve(MavenArtifactUtils.convertArtifactToPath(artifact)));
        generateArtifact(basedir, artifact);

        return artifact;
    }

    public void generateArtifact(Path basedir, MavenArtifact artifact)
            throws IOException,
                   XmlPullParserException,
                   NoSuchAlgorithmException
    {
        artifact.setPath(basedir.resolve(MavenArtifactUtils.convertArtifactToPath(artifact)));

        MavenArtifactGenerator generator = new MavenArtifactGenerator(basedir.toAbsolutePath().toString());
        generator.generate(artifact);
    }

    public void generateArtifact(String basedir, MavenArtifact artifact)
            throws IOException,
                   XmlPullParserException,
                   NoSuchAlgorithmException
    {
        artifact.setPath(Paths.get(basedir).resolve(MavenArtifactUtils.convertArtifactToPath(artifact)));

        MavenArtifactGenerator generator = new MavenArtifactGenerator(basedir);
        generator.generate(artifact);
    }

    public void generateArtifact(String basedir, MavenArtifact artifact, String packaging)
            throws IOException,
                   XmlPullParserException,
                   NoSuchAlgorithmException
    {
        artifact.setPath(Paths.get(basedir).resolve(MavenArtifactUtils.convertArtifactToPath(artifact)));

        MavenArtifactGenerator generator = new MavenArtifactGenerator(basedir);
        generator.generate(artifact, packaging);
    }

    public static void generateArtifact(String basedir, String gavtc, String... versions)
            throws IOException,
                   XmlPullParserException,
                   NoSuchAlgorithmException
    {
        MavenArtifactGenerator generator = new MavenArtifactGenerator(basedir);
        generator.generate(gavtc, versions);
    }

    public void generateArtifact(String basedir, String gavtc, String packaging, String... versions)
            throws IOException,
                   XmlPullParserException,
                   NoSuchAlgorithmException
    {
        MavenArtifactGenerator generator = new MavenArtifactGenerator(basedir);
        generator.generate(gavtc, packaging, versions);
    }

    public void generatePluginArtifact(String basedir, String gavtc, String... versions)
            throws IOException,
                   XmlPullParserException,
                   NoSuchAlgorithmException
    {
        MavenArtifactGenerator generator = new MavenArtifactGenerator(basedir);
        generator.generate(gavtc, "maven-plugin", versions);
    }

    public InputStream generateArtifactInputStream(String basedir, String repositoryId, String gavtc, boolean useTempDir)
            throws NoSuchAlgorithmException,
                   XmlPullParserException,
                   IOException
    {
        File baseDir = new File(basedir + "/" + repositoryId + (useTempDir ? "/.temp" : ""));
        if (!baseDir.exists())
        {
            //noinspection ResultOfMethodCallIgnored
            baseDir.mkdirs();
        }

        MavenArtifact artifact = generateArtifact(baseDir.getCanonicalPath(), gavtc);

        return new FileInputStream(new File(baseDir, MavenArtifactUtils.convertArtifactToPath(artifact)));
    }

    public MavenArtifact createTimestampedSnapshotArtifact(String repositoryBasedir,
                                                           String groupId,
                                                           String artifactId,
                                                           String baseSnapshotVersion)
            throws NoSuchAlgorithmException, XmlPullParserException, IOException
    {
        return createTimestampedSnapshotArtifact(repositoryBasedir,
                                                 groupId,
                                                 artifactId,
                                                 baseSnapshotVersion,
                                                 "jar",
                                                 null,
                                                 1);
    }

    public MavenArtifact createTimestampedSnapshotArtifact(String repositoryBasedir,
                                                           String groupId,
                                                           String artifactId,
                                                           String baseSnapshotVersion,
                                                           int numberOfBuilds)
            throws NoSuchAlgorithmException, XmlPullParserException, IOException
    {
        return createTimestampedSnapshotArtifact(repositoryBasedir,
                                                 groupId,
                                                 artifactId,
                                                 baseSnapshotVersion,
                                                 "jar",
                                                 null,
                                                 numberOfBuilds);
    }

    public MavenArtifact createTimestampedSnapshotArtifact(String repositoryBasedir,
                                                           String groupId,
                                                           String artifactId,
                                                           String baseSnapshotVersion,
                                                           String[] classifiers)
            throws NoSuchAlgorithmException, XmlPullParserException, IOException
    {
        return createTimestampedSnapshotArtifact(repositoryBasedir,
                                                 groupId,
                                                 artifactId,
                                                 baseSnapshotVersion,
                                                 "jar",
                                                 classifiers,
                                                 1);
    }

    public MavenArtifact createTimestampedSnapshotArtifact(String repositoryBasedir,
                                                           String groupId,
                                                           String artifactId,
                                                           String baseSnapshotVersion,
                                                           String packaging,
                                                           String[] classifiers)
            throws NoSuchAlgorithmException, XmlPullParserException, IOException
    {
        return createTimestampedSnapshotArtifact(repositoryBasedir,
                                                 groupId,
                                                 artifactId,
                                                 baseSnapshotVersion,
                                                 packaging,
                                                 classifiers,
                                                 1);
    }

    public MavenArtifact createTimestampedSnapshotArtifact(String repositoryBasedir,
                                                           String groupId,
                                                           String artifactId,
                                                           String baseSnapshotVersion,
                                                           String packaging,
                                                           String[] classifiers,
                                                           int numberOfBuilds)
            throws NoSuchAlgorithmException, XmlPullParserException, IOException
    {
        MavenArtifact artifact = null;

        for (int i = 0; i < numberOfBuilds; i++)
        {
            String version = createSnapshotVersion(baseSnapshotVersion, i + 1);

            artifact = new MavenDetachedArtifact(groupId, artifactId, version);
            artifact.setPath(Paths.get(repositoryBasedir).resolve(MavenArtifactUtils.convertArtifactToPath(artifact)));

            generateArtifact(repositoryBasedir, artifact, packaging);

            if (classifiers != null)
            {
                for (String classifier : classifiers)
                {
                    String gavtc = groupId + ":" + artifactId + ":" + version + ":jar:" + classifier;
                    generateArtifact(repositoryBasedir,MavenArtifactUtils.getArtifactFromGAVTC(gavtc));
                }
            }
        }

        // Return the main artifact
        return artifact;
    }

    public MavenArtifact createTimestampedSnapshot(String repositoryBasedir,
                                                   String groupId,
                                                   String artifactId,
                                                   String baseSnapshotVersion,
                                                   String packaging,
                                                   String[] classifiers,
                                                   int numberOfBuild,
                                                   String timestamp)
            throws NoSuchAlgorithmException, XmlPullParserException, IOException
    {
        MavenArtifact artifact;

        String version = createSnapshotVersion(baseSnapshotVersion, numberOfBuild, timestamp);

        artifact = new MavenDetachedArtifact(groupId, artifactId, version);
        artifact.setPath(Paths.get(repositoryBasedir).resolve(MavenArtifactUtils.convertArtifactToPath(artifact)));

        generateArtifact(repositoryBasedir, artifact, packaging);

        if (classifiers != null)
        {
            for (String classifier : classifiers)
            {
                String gavtc = groupId + ":" + artifactId + ":" + version + ":jar:" + classifier;
                generateArtifact(repositoryBasedir, MavenArtifactUtils.getArtifactFromGAVTC(gavtc));
            }
        }

        // Return the main artifact
        return artifact;
    }

    public String createSnapshotVersion(String baseSnapshotVersion, int buildNumber)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd.HHmmss");
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.SECOND, 7);
        calendar.add(Calendar.MINUTE, 5);

        String timestamp = formatter.format(calendar.getTime());
        @SuppressWarnings("UnnecessaryLocalVariable")
        String version = baseSnapshotVersion + "-" + timestamp + "-" + buildNumber;

        return version;
    }

    public String createSnapshotVersion(String baseSnapshotVersion,
                                        int buildNumber,
                                        String timestamp)
    {
        @SuppressWarnings("UnnecessaryLocalVariable")
        String version = baseSnapshotVersion + "-" + timestamp + "-" + buildNumber;

        return version;
    }

    /**
     * Generate a couple of testing artifacts for a specific snapshot (i.e. javadoc, sources, etc)
     *
     * @param repositoryBasedir String
     * @param gavt String
     * @throws NoSuchAlgorithmException
     * @throws XmlPullParserException
     * @throws IOException
     */
    public MavenArtifact createSnapshot(String repositoryBasedir, String gavt)
            throws NoSuchAlgorithmException, XmlPullParserException, IOException
    {
        MavenArtifact snapshot = MavenArtifactUtils.getArtifactFromGAVTC(gavt);
        snapshot.setFile(new File(repositoryBasedir + "/" + MavenArtifactUtils.convertArtifactToPath(snapshot)));

        generateArtifact(repositoryBasedir, snapshot);

        return snapshot;
    }

    /**
     * Generate a couple of testing artifacts for a specific snapshot (i.e. javadoc, sources, etc)
     *
     * @param repositoryBasedir String
     * @param gavt String
     * @throws NoSuchAlgorithmException
     * @throws XmlPullParserException
     * @throws IOException
     */
    public MavenArtifact createSnapshot(String repositoryBasedir, String gavt, String[] classifiers)
            throws NoSuchAlgorithmException, XmlPullParserException, IOException
    {
        MavenArtifact snapshot = MavenArtifactUtils.getArtifactFromGAVTC(gavt);
        snapshot.setFile(new File(repositoryBasedir + "/" + MavenArtifactUtils.convertArtifactToPath(snapshot)));

        generateArtifact(repositoryBasedir, snapshot);

        for (String classifier : classifiers)
        {
            generateArtifact(repositoryBasedir, MavenArtifactUtils.getArtifactFromGAVTC(gavt + ":" + classifier));

        }

        return snapshot;
    }

    public void changeCreationDate(MavenArtifact artifact)
            throws IOException
    {
        File directory = artifact.getPath().getParent().toFile();

        //noinspection ConstantConditions
        for (final File fileEntry : directory.listFiles())
        {
            if (fileEntry.isFile())
            {
                BasicFileAttributeView attributes = Files.getFileAttributeView(fileEntry.toPath(), BasicFileAttributeView.class);
                FileTime time = FileTime.from(System.currentTimeMillis() + 60000L, TimeUnit.MILLISECONDS);
                attributes.setTimes(time, time, time);
            }
        }
    }

    public File getStorageBasedir(String storageId)
    {
        return new File(ConfigurationResourceResolver.getVaultDirectory() + "/storages/" + storageId);
    }

    public File getRepositoryBasedir(String storageId, String repositoryId)
    {
        return new File(ConfigurationResourceResolver.getVaultDirectory() + "/storages/" + storageId + "/" + repositoryId);
    }

    public MavenRepositoryFeatures getFeatures()
    {
        return features;
    }

    public RepositoryManagementStrategy getManagementStrategy(String storageId,
                                                              String repositoryId)
    {
        Storage storage = getConfiguration().getStorage(storageId);
        Repository repository = storage.getRepository(repositoryId);

        LayoutProvider layoutProvider = layoutProviderRegistry.getProvider(repository.getLayout());

        return layoutProvider.getRepositoryManagementStrategy();
    }

}
