package util.samplegenerator;

import database.DatabaseOperator;
import util.samplegenerator.util.GaussianGenerator;

import java.sql.SQLException;

public class SamplesGenerator {

    /**
     * to generate examples and add them to the database.
     * 
     * @param avg_item_num specify which of the item_number as the middle of them
     * @param avg_item_amount specify how many items are tend to appear most in each of transaction
     * @param item_standard_variance the variance of item_number distribution
     * @param item_amount_standard_variance the variance of item amount, that is the length
     * @param minLength minimum length of single transaction
     * @param maxLength maximum length of a single transaction
     */
    public static void generate(
            int item_amount,
            int transaction_amount,
            int avg_item_num,
            int avg_item_amount,
            double item_standard_variance,
            double item_amount_standard_variance,
            int minLength,
            int maxLength
    ) {
        GaussianGenerator itemGenerator = new GaussianGenerator(avg_item_num, item_standard_variance, 0, item_amount + 1);
        GaussianGenerator lengthGenerator = new GaussianGenerator(avg_item_amount, item_amount_standard_variance, minLength, maxLength + 1);

        DatabaseOperator.startInsert();

        for (int i = 0; i < transaction_amount; ++i) {
            StringBuilder builder = new StringBuilder(
                    "insert into transactions values ("
            ).append(i).append(", ");
            int lp = builder.length();
            int l = lengthGenerator.getNextInt();

            for (int j = 0; j < l; ++j) {
                // unknown if lp == builder.length(), will it throw exception
                builder.delete(lp, builder.length())
                        .append(itemGenerator.getNextInt())
                        .append(")");

                try {
                    DatabaseOperator.continueInsert(builder.toString());
                } catch (SQLException e) {
                    --j;
                }
            }
        }

        DatabaseOperator.endInsert();
    }

    public static void generate(int item_amount, int transaction_amount) {
        int min = item_amount - (int) (0.8 * item_amount),
                max = item_amount - (int) (0.2 * item_amount),
                avg_amount = item_amount / 2;
        SamplesGenerator.generate(
                item_amount,
                transaction_amount,
                item_amount / 2,
                avg_amount,
                1.96 * item_amount / 2,
                1.96 * (max - avg_amount),
                min,
                max
        );
    }
}
