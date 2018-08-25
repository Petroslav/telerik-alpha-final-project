package com.alpha.marketplace;

import org.kohsuke.github.*;

import java.io.IOException;

public class Gittest {
    public static void main(String[] args) throws IOException {
//        GitHub gh = GitHub.connectAnonymously();
        GitHub gh1 = GitHub.connectUsingOAuth("5c1a77eec3047ae6b562a55a7c0e4d4735cb38ef");
        GHRepository gr = gh1.getRepository("Petroslav/telerik-alpha-final-project");
        System.out.println(gr.listCommits().asList().get(0).getCommitDate().toString());
    }
}
