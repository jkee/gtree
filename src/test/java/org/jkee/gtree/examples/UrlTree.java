package org.jkee.gtree.examples;

import com.google.common.collect.Lists;
import org.jkee.gtree.Forest;
import org.jkee.gtree.builder.PathTreeBuilder;

import java.util.List;

/**
 * todo
 *
 * We have a batch of urls and want to build a tree
 * For simplicity, let it be only a path part
 *
 * @author jkee
 */

public class UrlTree {
    public static void main(String[] args) {
        PathTreeBuilder.Funnel<String, String> urlFunnel = new PathTreeBuilder.Funnel<String, String>() {
            @Override
            public List<String> getPath(String value) {
                return Lists.newArrayList(value.split("/"));
            }
        };
        List<String> urls = Lists.newArrayList(
                "/home/url1/suburl",
                "/home/url1",
                "/home/url2",
                "/basket",
                "/basket/url1",
                "/basket/url2",
                "/home/url1/suburl/more",
                "/about",
                "/home/url3/more"
        );
        PathTreeBuilder<String, String> builder = new PathTreeBuilder<String, String>(urlFunnel);
        Forest<String> build = builder.build(urls);
        System.out.println(build.toStringTree());
    }
}
