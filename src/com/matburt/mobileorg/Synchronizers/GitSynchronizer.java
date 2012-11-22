package com.matburt.mobileorg.Synchronizers;

import java.io.File;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.matburt.mobileorg.util.FileUtils;

public class GitSynchronizer implements SynchronizerInterface {

    private Context context;    

    private String git_remote_url;
    private String git_local_url;

    private Repository repository_local;
    private Repository repository_remote;


    public GitSynchronizer(Context context) {
        this( context,
              (PreferenceManager.getDefaultSharedPreferences(context)).getString("gitRemoteUrl","/sdcard/mobileorg_git_remote"),
              (PreferenceManager.getDefaultSharedPreferences(context)).getString("gitLocalUrl","/sdcard/mobileorg_git_local"));
    }

    public GitSynchronizer(Context context, String remote_url, String local_url) {
        this.git_remote_url = remote_url;
        this.git_local_url = local_url;

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            // repository_local = builder.setGitDir(new File( git_local_url ))
            //     .readEnvironment()
            //     .findGitDir()
            //     .build();
            // Config config = repository_local.getConfig();
            // config.setString("remote","origin", git_remote_url );

            // repository_local = (new CloneCommand())
            //     .setURI( git_remote_url )
            //     .setDirectory( new File( git_local_url ) )
            //     .call().getRepository();

            repository_local = builder.setGitDir(new File( git_local_url + "/.git" ))
                .readEnvironment()
                .findGitDir()
                .build();
            
        } catch ( Exception e ) {
            repository_local = null;
            Log.e("OrgMobile: GitSynchronizer","Error setting up git repository: " + repository_local );
            Log.e("OrgMobile: GitSynchronizer","  " + e.getLocalizedMessage() );
        }
    }

    public boolean isConfigured() {
        // TODO Check existence of git repository in the file system
        if( repository_local != null ) {
            return true;
        } else {
            return false;
        }
    }

    public void putRemoteFile( String filename, String contents) throws IOException, GitAPIException {
        Log.d("Orgmobile.GitSynchronizer","Putting file: " + filename + " with contents: " + contents);
	// FileUtils orgFile = new FileUtils(filename, context);
        // orgFile.getWriter();
        BufferedWriter writer =  new BufferedWriter( new FileWriter( git_local_url + "/" + filename ) );
        writer.write(contents);
        writer.close();

        Log.d("Orgmobile.GitSynchronizer","Staging file: " + filename + ".");
        (new AddCommand( repository_local ))
            .addFilepattern( filename )
            .call();
        Log.d("Orgmobile.GitSynchronizer","Committing: " + filename + ".");
        Git git = new Git( repository_local );
        git.commit()
            .setMessage("[OrgMobile]: Changed " + filename )
            .call();
        Log.d("Orgmobile.GitSynchronizer","Commit successful!");

        Log.d("Orgmobile.GitSynchronizer","Pushing...");
        git = new Git( repository_local );
        git.push().call();
        Log.d("Orgmobile.GitSynchronizer","Push successful!");


    }

    public BufferedReader getRemoteFile(String filename) throws IOException, GitAPIException, SynchronizeException {
        Log.d("Orgmobile.GitSynchronizer","Getting remote file: " + filename);
        pull();
        return new BufferedReader( new FileReader( git_local_url + "/" + filename ) );
    }

    public void pull() throws GitAPIException, SynchronizeException {
        Log.d("Orgmobile.GitSynchronizer","Pulling...");
        Git git = new Git( repository_local );
        PullResult result = git.pull()
            .call();

        if( !result.isSuccessful() ) {
            throw new SynchronizeException("Git pull command failed!");
        }
        Log.d("Orgmobile.GitSynchronizer","Pull successful!");
    }

    public void postSynchronize() { };

}
