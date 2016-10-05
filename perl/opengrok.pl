use strict;
use File::Path;
use JSON::Parse 'json_file_to_perl';
use Data::Dumper;

my $catalina_home = "/usr/share/apache-tomcat-7.0.41";             # TODO: read from ENV
my $opengrok_home = "/usr/share/opengrok-0.11.1";                  # TODO: read from ENV
my $opengrok_workspace = "/export/home/tomcat/opengrok-workspace"; # TODO: read from ENV
my $ctags_home = "/usr/share/ctags-5.8";                           # TODO: read from ENV

# Clean up from previous clone
#rmtree($opengrok_workspace, { safe => 1, keep_root => 1 });
system("rm -rf $opengrok_workspace/*");

chdir($opengrok_workspace);

# Get repository list for each Stash project
my @stash_projects = ("ULVER", "KENNY", "BISKER", "MOGENS", "JIM");
foreach my $stash_project (@stash_projects) {
  clone_stash_repos($stash_project);
}

# Call OpenGrok jar with appropriate command-line arguments
chdir($opengrok_home);
print "Running OpenGrok...\n";
`java -classpath . -Xmx1524m -Djava.util.logging.config.file=logging.properties -jar lib/opengrok.jar -W $opengrok_home/configuration.xml -r on -a on -P -S -v -s $opengrok_workspace -d $opengrok_home/data -c $ctags_home/ctags`;

# Restart Tomcat such that the OpenGrok WAR (source.war) is reloaded
chdir($catalina_home) or die "$!";
print "Restarting Tomcat...\n";
`bin/catalina.sh stop 30` or die "$!";
`bin/catalina.sh start` or die "$!";

print "Done!\n";

sub clone_stash_repos {
  my $stash_project = $_[0];

  # authenticate via HTTP to be able to use the REST API
  my $curl_command = "curl -u <user>:<pass> --insecure https://<host>:8443/rest/api/1.0/projects/" . $stash_project . "/repos > " . $stash_project . "_repos.txt";
  `$curl_command`;
  my $json = json_file_to_perl ($stash_project . "_repos.txt");
  #print Dumper($json);

  my $values = $ {$json} { values };
  my @values = @{ $values };
  #print "length: " . scalar @values . "\n";

  # Get latest code from remote repositories (git clone)
  foreach my $value (@values) {
    my $clone_url = $value->{'cloneUrl'};
    #print "clone url (before): $clone_url\n";
    $clone_url =~ s|^(https://<user>)(.*)$|\1:<pass>\2|;
    #print "clone url (after): $clone_url\n";
    print "Cloning into $clone_url...\n";
    `git clone $clone_url` or die "$!";
  }
}
