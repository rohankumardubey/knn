package com.maxdemarzi;

import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;
import org.roaringbitmap.RoaringBitmap;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

public class Procedures {

    // This field declares that we need a GraphDatabaseService
    // as context when any procedure in this class is invoked
    @Context
    public GraphDatabaseService db;

    // This gives us a log instance that outputs messages to the
    // standard log, normally found under `data/log/console.log`
    @Context
    public Log log;


    @Procedure(name = "com.maxdemarzi.knnx", mode = Mode.READ)
    @Description("CALL com.maxdemarzi.knnx(Node node, Number n)")
    public Stream<NumberResult> knnx(@Name("node") Node node, @Name("n") Number n) throws IOException {
        // Initialize bitmaps for iteration
        RoaringBitmap seen = new RoaringBitmap();
        RoaringBitmap nextA = new RoaringBitmap();
        RoaringBitmap nextB = new RoaringBitmap();
        seen.add((int) node.getId());

        nextA.add((int) node.getId());

        // First Hop
        Iterator<Integer> iterator = nextA.iterator();
        while (iterator.hasNext()) {
            int nodeId = iterator.next();
            for (Relationship r : db.getNodeById((long) nodeId).getRelationships(Direction.OUTGOING)) {
                if (seen.checkedAdd((int) r.getEndNodeId())) {
                    nextB.add((int) r.getEndNodeId());
                }
            }
        }
        for(int i = 1; i < n.intValue(); i++) {

            nextA.clear();

            // next even Hop
            iterator = nextB.iterator();
            while (iterator.hasNext()) {
                int nodeId = iterator.next();
                for (Relationship r : db.getNodeById((long) nodeId).getRelationships(Direction.OUTGOING)) {
                    if (seen.checkedAdd((int) r.getEndNodeId())) {
                        nextA.add((int) r.getEndNodeId());
                    }
                }
            }

            i++;
            if (i < n.intValue()) {
                nextB.clear();

                // next odd Hop
                iterator = nextA.iterator();
                while (iterator.hasNext()) {
                    int nodeId = iterator.next();
                    for (Relationship r : db.getNodeById((long) nodeId).getRelationships(Direction.OUTGOING)) {
                        if (seen.checkedAdd((int) r.getEndNodeId())) {
                            nextB.add((int) r.getEndNodeId());
                        }
                    }
                }
            }
        }
        // remove myself
        seen.remove((int) node.getId());

        return  Stream.of(new NumberResult(seen.getCardinality()));
    }


    @Procedure(name = "com.maxdemarzi.knn", mode = Mode.READ)
    @Description("CALL com.maxdemarzi.knn(Node node)")
    public Stream<NumberResult> knn(@Name("node") Node node) throws IOException {
        // Initialize bitmaps for iteration
        RoaringBitmap seen = new RoaringBitmap();
        RoaringBitmap nextA = new RoaringBitmap();
        RoaringBitmap nextB = new RoaringBitmap();
        seen.add((int) node.getId());

        nextA.add((int) node.getId());

        // First Hop
        Iterator<Integer> iterator = nextA.iterator();
        while (iterator.hasNext()) {
            int nodeId = iterator.next();
            for (Relationship r : db.getNodeById((long) nodeId).getRelationships(Direction.OUTGOING)) {
                if (seen.checkedAdd((int)r.getEndNodeId())) {
                    nextB.add((int)r.getEndNodeId());
                }
            }
        }

        nextA.clear();

        // Second Hop
        iterator = nextB.iterator();
        while (iterator.hasNext()) {
            int nodeId = iterator.next();
            for (Relationship r : db.getNodeById((long) nodeId).getRelationships(Direction.OUTGOING)) {
                if(seen.checkedAdd((int)r.getEndNodeId())) {
                    nextA.add((int)r.getEndNodeId());
                }
            }
        }

        nextB.clear();

        // Third Hop
        iterator = nextA.iterator();
        while (iterator.hasNext()) {
            int nodeId = iterator.next();
            for (Relationship r : db.getNodeById((long) nodeId).getRelationships(Direction.OUTGOING)) {
                if (seen.checkedAdd((int)r.getEndNodeId())) {
                    nextB.add((int)r.getEndNodeId());
                }
            }
        }

        nextA.clear();

        // Fourth Hop
        iterator = nextB.iterator();
        while (iterator.hasNext()) {
            int nodeId = iterator.next();
            for (Relationship r : db.getNodeById((long) nodeId).getRelationships(Direction.OUTGOING)) {
                if(seen.checkedAdd((int)r.getEndNodeId())) {
                    nextA.add((int)r.getEndNodeId());
                }
            }
        }

        nextB.clear();

        // Fifth Hop
        iterator = nextA.iterator();
        while (iterator.hasNext()) {
            int nodeId = iterator.next();
            for (Relationship r : db.getNodeById((long) nodeId).getRelationships(Direction.OUTGOING)) {
                if (seen.checkedAdd((int)r.getEndNodeId())) {
                    nextB.add((int)r.getEndNodeId());
                }
            }
        }

        nextA.clear();

        // Sixth Hop
        iterator = nextB.iterator();
        while (iterator.hasNext()) {
            int nodeId = iterator.next();
            for (Relationship r : db.getNodeById((long) nodeId).getRelationships(Direction.OUTGOING)) {
                if(seen.checkedAdd((int)r.getEndNodeId())) {
                    nextA.add((int)r.getEndNodeId());
                }
            }
        }

        nextB.clear();

        // Seventh Hop
        iterator = nextA.iterator();
        while (iterator.hasNext()) {
            int nodeId = iterator.next();
            for (Relationship r : db.getNodeById((long) nodeId).getRelationships(Direction.OUTGOING)) {
                if (seen.checkedAdd((int)r.getEndNodeId())) {
                    nextB.add((int)r.getEndNodeId());
                }
            }
        }

        // remove myself
        seen.remove((int) node.getId());

        return  Stream.of(new NumberResult(seen.getCardinality()));
    }
}