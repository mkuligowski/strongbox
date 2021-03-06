package org.carlspring.strongbox.providers.repository;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.carlspring.strongbox.client.ArtifactTransportException;
import org.carlspring.strongbox.data.criteria.Paginator;
import org.carlspring.strongbox.data.criteria.Predicate;
import org.carlspring.strongbox.data.criteria.Selector;
import org.carlspring.strongbox.io.RepositoryInputStream;
import org.carlspring.strongbox.io.RepositoryOutputStream;
import org.carlspring.strongbox.providers.ProviderImplementationException;

/**
 * @author carlspring
 */
public interface RepositoryProvider
{

    String getAlias();

    RepositoryInputStream getInputStream(String storageId, String repositoryId, String path)
            throws IOException,
                   NoSuchAlgorithmException,
                   ArtifactTransportException,
                   ProviderImplementationException;

    RepositoryOutputStream getOutputStream(String storageId,
                                           String repositoryId,
                                           String path)
            throws IOException, NoSuchAlgorithmException;
    
    List<Path> search(RepositorySearchRequest searchRequest, RepositoryPageRequest pageRequest);
    
    Long count(RepositorySearchRequest searchRequest);
    
    List<Path> search(String storageId,
                      String repositoryId,
                      Predicate predicate,
                      Paginator paginator);
    
    Long count(String storageId,
               String repositoryId,
               Predicate predicate);
    
    Path resolvePath(String storageId,
                     String repositoryId,
                     String artifactPath) 
            throws IOException;

}
