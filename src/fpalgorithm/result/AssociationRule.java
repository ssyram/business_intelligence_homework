package fpalgorithm.result;

import java.util.Set;

public class AssociationRule {
    private Set<Integer> a, b;
    private double support, confidence;

    public AssociationRule(Set<Integer> A, Set<Integer> B, double support, double confidence) {
        a = A; b = B;
        this.support = support; this.confidence = confidence;
    }

    public double getSupport() {
        return support;
    }

    public double getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("(");
        for (Integer i: a)
            builder.append(i).append(", ");
        builder.replace(builder.length() - 2, builder.length(), ") -> (");

        for (Integer i: b)
            builder.append(i).append(", ");
        builder.replace(builder.length() - 2, builder.length(), ")[");
        builder.append(support).append(", ").append(confidence).append("]");

        return builder.toString();
    }
}
