package ch.unibas.dmi.dbis.polyphenydb.client.main;


import ch.unibas.dmi.dbis.polyphenydb.client.main.analysis.TPCCAnalysis;
import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.help.Help;


/**
 * Be aware that you have to be inside the VPN to upload files to chronos.dmi.unibas.ch
 */
@Cli(
        name = "polypheny-db-client",
        description = "A client for DBMS which dynamically replicate and partition big data. The client is able to stress the DBMS with different benchmarks and scenarios.",
        commands = { MasterCommand.class, WorkerCommand.class, LocalCommand.class, Help.class, ConsoleCommand.class, TPCCAnalysis.class },
        defaultCommand = Help.class)
public class Main {

    public static void main( String[] args ) {
        new com.github.rvesse.airline.Cli<Runnable>( Main.class ).parse( args ).run();
    }
}
