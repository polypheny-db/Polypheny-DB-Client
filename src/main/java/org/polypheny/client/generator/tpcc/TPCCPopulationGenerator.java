package org.polypheny.client.generator.tpcc;


import static org.polypheny.client.generator.RandomGenerator.generateAString;
import static org.polypheny.client.generator.RandomGenerator.generateNString;
import static org.polypheny.client.generator.RandomGenerator.generateUniform;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.polypheny.client.config.TPCCConfig;
import org.polypheny.client.generator.tpcc.objects.Customer;
import org.polypheny.client.generator.tpcc.objects.District;
import org.polypheny.client.generator.tpcc.objects.History;
import org.polypheny.client.generator.tpcc.objects.Item;
import org.polypheny.client.generator.tpcc.objects.NewOrder;
import org.polypheny.client.generator.tpcc.objects.Order;
import org.polypheny.client.generator.tpcc.objects.OrderLine;
import org.polypheny.client.generator.tpcc.objects.Stock;
import org.polypheny.client.generator.tpcc.objects.Warehouse;


/**
 * Generator for the Population of a TPC-C Database. Follows Section 4 of the TPC-C Specifications. <p>
 *
 * @author Silvan Heller
 * @tpccversion 5.11
 */
public class TPCCPopulationGenerator {

    /**
     * See 4.3.2.3
     */
    public static final String[] C_LAST_SYLLABLES = { "BAR", "OUGHT", "ABLE", "PRI", "PRES", "ESE",
            "ANTI", "CALLY", "ATION", "EING" };
    private static final Logger logger = LogManager.getLogger();


    /**
     * See 4.3.2.3 Generates Customer Last Name (C_LAST)
     *
     * @param number 0 and 999.
     */
    public static String generateC_LAST( int number ) {
        StringBuilder sb = new StringBuilder();
        int h = (number - ((number % 100) - (number % 10))) / 100;
        int t = ((number % 100) - (number % 10)) / 10;
        int o = number % 10;
        sb.append( C_LAST_SYLLABLES[h] );
        sb.append( C_LAST_SYLLABLES[t] );
        sb.append( C_LAST_SYLLABLES[o] );
        return sb.toString();
    }


    /**
     * See 4.3.2.7. ZIPs are generated by concatenating a random n-string of l=4 and the string '11111'.
     *
     * @return An Integer of format xxxx11111
     */
    public static int generateZIP() {
        String randomN = generateNString( 4, 4 );
        return Integer.parseInt( randomN + "11111" );
    }


    /**
     * Generates {@link Item} objects according to 4.3.3.1
     *
     * @return An Array of length {@link TPCCConfig#NUMBER_OF_ITEMS}
     */
    public static Item[] generateItems() {
        logger.trace( "generating items" );
        long start = System.currentTimeMillis();
        Item[] items = new Item[TPCCConfig.NUMBER_OF_ITEMS];
        for ( int i = 0; i < TPCCConfig.NUMBER_OF_ITEMS; i++ ) {
            items[i] = generateItem( i + 1 );
        }
        long stop = System.currentTimeMillis();
        logger.trace( "finished item generation. Elapsed time: {} ms", stop - start );
        return items;
    }


    /**
     * Generates an {@link Item} object with a given I_ID. Uses %10 to determine whether 'ORIGINAL' will be inserted into I_DATA
     *
     * @param I_ID unique item ID which will be used for this item
     */
    public static Item generateItem( final int I_ID ) {
        String I_DATA = generateOriginalAString( 26, 50, generateUniform( 1, 10 ) == 1 );
        int I_IM_ID = generateUniform( 1, 10_000 );
        String I_NAME = generateAString( 14, 24 );
        Double I_PRICE = generateUniform( 1, 99 ) + generateUniform( 1, 100 ) / 100d;    //99 because we add [0,1~to it
        return new Item( I_ID, I_IM_ID, I_NAME, I_PRICE, I_DATA );
    }


    /**
     * Generates one {@link Warehouse} object with a given W_ID According to 4.3.3.1
     *
     * @param W_ID Unique within [number_of_configured_warehouses]
     */
    public static Warehouse generateWarehouse( final int W_ID ) {
        String W_NAME = generateAString( 6, 10 );
        String W_STREET_1 = generateAString( 10, 20 );
        String W_STREET_2 = generateAString( 10, 20 );
        String W_CITY = generateAString( 10, 20 );
        String W_STATE = generateAString( 2, 2 );
        int W_ZIP = generateZIP();
        Double W_TAX = generateUniform( 0, 2000 ) / 10_000d;
        Double W_YTD = 300_000d;
        return new Warehouse( W_ID, W_NAME, W_STREET_1, W_STREET_2, W_CITY, W_STATE, W_ZIP, W_TAX, W_YTD );
    }


    /**
     * Generates {@link Warehouse} objects with IDs from 1 to NUMBER_OF_CONFIGURED WAREHOUSES
     *
     * @param NUMBER_OF_CONFIGURED_WAREHOUSES how many {@link Warehouse} should be generated
     */
    public static Warehouse[] generateWarehouses( final int NUMBER_OF_CONFIGURED_WAREHOUSES ) {
        Warehouse[] warehouses = new Warehouse[NUMBER_OF_CONFIGURED_WAREHOUSES];
        for ( int i = 0; i < NUMBER_OF_CONFIGURED_WAREHOUSES; i++ ) {
            warehouses[i] = generateWarehouse( i + 1 );
        }
        return warehouses;
    }


    /**
     * Generates one stock object for a given {@link Warehouse}
     *
     * @param warehouse The {@link Warehouse} this {@link Stock} belongs to
     * @param S_I_ID unique stock-id within [100,000]
     */
    public static Stock generateStock( Warehouse warehouse, int S_I_ID ) {
        int S_W_ID = warehouse.getW_ID();
        int S_QUANTITY = generateUniform( 10, 100 );
        String S_DIST_01 = generateAString( 24, 24 );
        String S_DIST_02 = generateAString( 24, 24 );
        String S_DIST_03 = generateAString( 24, 24 );
        String S_DIST_04 = generateAString( 24, 24 );
        String S_DIST_05 = generateAString( 24, 24 );
        String S_DIST_06 = generateAString( 24, 24 );
        String S_DIST_07 = generateAString( 24, 24 );
        String S_DIST_08 = generateAString( 24, 24 );
        String S_DIST_09 = generateAString( 24, 24 );
        String S_DIST_10 = generateAString( 24, 24 );
        int S_YTD = 0;
        int S_ORDER_CNT = 0;
        int S_REMOTE_CNT = 0;
        String S_DATA = generateOriginalAString( 26, 50, generateUniform( 1, 10 ) == 1 );
        return new Stock( S_I_ID, S_W_ID, S_QUANTITY, S_DIST_01, S_DIST_02, S_DIST_03, S_DIST_04, S_DIST_05, S_DIST_06, S_DIST_07, S_DIST_08, S_DIST_09, S_DIST_10, S_YTD, S_ORDER_CNT, S_REMOTE_CNT, S_DATA );
    }


    /**
     * Generates {@link Stock} objects for one {@link Warehouse} according to 4.3.3.1
     *
     * @param warehouse Which warehouse the stock should be generated for.
     */
    public static Stock[] generateStockForWarehouse( final Warehouse warehouse ) {
        logger.trace( "Generating stock" );
        long start = System.currentTimeMillis();
        Stock[] stocks = new Stock[TPCCConfig.STOCK_PER_WAREHOUSE];
        for ( int i = 0; i < TPCCConfig.STOCK_PER_WAREHOUSE; i++ ) {
            stocks[i] = generateStock( warehouse, i + 1 );
        }
        logger.trace( "Finished generating stock. Elapsed time {} ms",
                (System.currentTimeMillis() - start) );
        return stocks;
    }


    /**
     * Generates one {@link District} according to 4.3.3.1
     *
     * @param warehouse {@link Warehouse} this district should belong to
     * @param D_ID unique within [10]
     */
    public static District generateDistrict( final Warehouse warehouse, final int D_ID ) {
        int D_W_ID = warehouse.getW_ID();
        String D_NAME = generateAString( 6, 10 );
        String D_STREET_1 = generateAString( 10, 20 );
        String D_STREET_2 = generateAString( 10, 20 );
        String D_CITY = generateAString( 10, 20 );
        String D_STATE = generateAString( 2, 2 );
        int D_ZIP = generateZIP();
        Double D_TAX = generateUniform( 0, 2000 ) / 10000d;
        Double D_YTD = 30000d;
        int D_NEXT_O_ID = 3001;
        return new District( D_ID, D_W_ID, D_NAME, D_STREET_1, D_STREET_2, D_CITY, D_STATE, D_ZIP, D_TAX, D_YTD, D_NEXT_O_ID );
    }


    /**
     * Generates all {@link District} objects for one {@link Warehouse}.
     */
    public static District[] generateDistrictsForWarehouse( Warehouse warehouse ) {
        District[] districts = new District[TPCCConfig.DISTRICTS_PER_WAREHOUSE];
        for ( int i = 0; i < TPCCConfig.DISTRICTS_PER_WAREHOUSE; i++ ) {
            districts[i] = generateDistrict( warehouse, i + 1 );
        }
        return districts;
    }


    /**
     * Generates one {@link Customer} object according to 4.3.3.1
     *
     * @param district {@link District} this {@link Customer} belongs to
     * @param C_LAST Different depending on {@link Customer}. See 4.3.3.1 for a detailed description.
     * @param C_SINCE date/time given by the operating system when the CUSTOMER table was created
     */
    public static Customer generateCustomer( final District district, final int C_ID,
            final String C_LAST, final Timestamp C_SINCE ) {
        int C_D_ID = district.getD_ID();
        int C_W_ID = district.getD_W_ID();
        String C_MIDDLE = "OE";
        String C_FIRST = generateAString( 8, 16 );
        String C_STREET_1 = generateAString( 10, 20 );
        String C_STREET_2 = generateAString( 10, 20 );
        String C_CITY = generateAString( 10, 20 );
        String C_STATE = generateAString( 2, 2 );
        int C_ZIP = generateZIP();
        String C_PHONE = generateNString( 16, 16 );
        String C_CREDIT;
        if ( generateUniform( 1, 10 ) == 1 ) {
            C_CREDIT = "BC";
        } else {
            C_CREDIT = "GC";
        }
        Double C_CREDIT_LIM = 50_000d;
        Double C_DISCOUNT = generateUniform( 0, 5_000 ) / 10_000d;
        Double C_BALANCE = -10d;
        Double C_YTD_PAYMENT = 10d;
        int C_PAYMENT_CNT = 1;
        int C_DELIVERY_CNT = 0;
        String C_DATA = generateAString( 300, 500 );
        return new Customer( C_ID, C_D_ID, C_W_ID, C_LAST, C_MIDDLE, C_FIRST, C_STREET_1, C_STREET_2, C_CITY, C_STATE, C_ZIP, C_PHONE, C_SINCE, C_CREDIT, C_CREDIT_LIM, C_DISCOUNT, C_BALANCE, C_YTD_PAYMENT, C_PAYMENT_CNT, C_DELIVERY_CNT, C_DATA );
    }


    /**
     * Generates {@link Customer}s for a given {@link District} according to 4.3.3.1
     *
     * @param district {@link District} to which these {@link Customer}s are assigned
     * @param C_SINCE date/time given by the operating system since when the CUSTOMER table was populated
     */
    public static Customer[] generateCustomersForDistrict( final District district,
            final Timestamp C_SINCE ) {
        Customer[] customers = new Customer[TPCCConfig.CUSTOMERS_PER_DISTRICT];
        for ( int i = 0; i < TPCCConfig.CUSTOMERS_PER_DISTRICT; i++ ) {
            int C_ID = i + 1;
            int C_LAST_NUMBER;
            if ( i < 1_000 ) {
                C_LAST_NUMBER = i;
            } else {
                int C_LOAD = TPCCGenerator.getcLoad();
                C_LAST_NUMBER = TPCCGenerator.getCLast( C_LOAD );
            }
            String C_LAST = generateC_LAST( C_LAST_NUMBER );
            customers[i] = generateCustomer( district, C_ID, C_LAST, C_SINCE );
        }
        return customers;
    }


    /**
     * @param customer {@link Customer} this {@link History} entry belongs to
     */
    public static History generateHistory( final Customer customer ) {
        int H_C_ID = customer.getC_ID();
        int H_C_D_ID = customer.getC_D_ID();
        int H_C_W_ID = customer.getC_W_ID();
        int H_D_ID = H_C_D_ID;
        int H_W_ID = H_C_W_ID;
        Timestamp H_DATE = Timestamp.from( Instant.now() );
        Double H_AMOUNT = 10d;
        String H_DATA = generateAString( 12, 24 );
        return new History( H_C_ID, H_C_D_ID, H_C_W_ID, H_DATE, H_AMOUNT, H_DATA, H_D_ID, H_W_ID );
    }


    /**
     * Generates initial {@link History} for a given {@link Customer}.
     */
    public static History[] generateHistoryForCustomer( final Customer customer ) {
        History[] histories = new History[TPCCConfig.HISTORY_ROWS_PER_CUSTOMER];
        for ( int i = 0; i < TPCCConfig.HISTORY_ROWS_PER_CUSTOMER; i++ ) {
            histories[i] = generateHistory( customer );
        }
        return histories;
    }


    /**
     * Generates one {@link Order}.
     */
    public static Order generateOrder( final int C_ID, final District district, final int O_ID ) {
        int O_C_ID = C_ID;
        int O_D_ID = district.getD_ID();
        int O_W_ID = district.getD_W_ID();
        Timestamp O_ENTRY_D = Timestamp.from( Instant.now() );
        Integer O_CARRIER_ID;
        if ( O_ID < 2101 ) {
            O_CARRIER_ID = generateUniform( 1, 10 );
        } else {
            O_CARRIER_ID = null;
        }
        int O_OL_CNT = generateUniform( 5, 15 );
        int O_ALL_LOCAL = 1;
        return new Order( O_ID, O_C_ID, O_D_ID, O_W_ID, O_ENTRY_D, O_CARRIER_ID, O_OL_CNT,
                O_ALL_LOCAL );
    }


    /**
     * Generates all {@link Order} objects for one {@link District} according to specification 4.3.3.1.
     */
    public static Order[] generateOrdersForDistrict( final District district ) {
        Order[] orders = new Order[TPCCConfig.ORDER_ROWS_PER_DISTRICT];
        //shuffle customerids
        int[] customerIDs = IntStream.range( 1, TPCCConfig.ORDER_ROWS_PER_DISTRICT + 1 )
                .toArray(); //Bleh. The range is exclusive for the upper end
        List<Integer> list = new ArrayList<>();
        for ( int customerID : customerIDs ) {
            list.add( customerID );
        }
        Collections.shuffle( list );
        for ( int i = 0; i < TPCCConfig.ORDER_ROWS_PER_DISTRICT; i++ ) {
            int O_ID = i + 1;
            int C_ID = list.get( i );
            orders[i] = generateOrder( C_ID, district, O_ID );
        }
        return orders;
    }


    /**
     * Generates one {@link OrderLine} object.
     *
     * @param OL_NUMBER should be unique within [O_OL_CNT]
     */
    public static OrderLine generateOrderLine( final Order order, final int OL_NUMBER ) {
        int OL_O_ID = order.getO_ID();
        int OL_D_ID = order.getO_D_ID();
        int OL_W_ID = order.getO_W_ID();
        int OL_I_ID = generateUniform( 1, 100000 );
        int OL_SUPPLY_W_ID = order.getO_W_ID();
        Timestamp OL_DELIVERY_D;
        if ( OL_O_ID < 2101 ) {
            OL_DELIVERY_D = order.getO_ENTRY_D();
        } else {
            OL_DELIVERY_D = null;
        }
        int OL_QUANTITY = 5;
        Double OL_AMOUNT;
        if ( OL_O_ID < 2101 ) {
            OL_AMOUNT = 0d;
        } else {
            OL_AMOUNT = generateUniform( 0, 9999 ) + generateUniform( 1, 99 ) / 100d;
        }
        String OL_DIST_INFO = generateAString( 24, 24 );
        return new OrderLine( OL_O_ID, OL_D_ID, OL_W_ID, OL_NUMBER, OL_I_ID, OL_SUPPLY_W_ID,
                OL_DELIVERY_D, OL_QUANTITY, OL_AMOUNT, OL_DIST_INFO );
    }


    /**
     * Generates {@link OrderLine} objects for one {@link Order} according to the TPC-C Specifications.
     */
    public static OrderLine[] generateOrderLineForOrder( final Order order ) {
        int O_OL_CNT = order.getO_OL_CNT();
        OrderLine[] orderLines = new OrderLine[O_OL_CNT];
        for ( int i = 0; i < O_OL_CNT; i++ ) {
            int OL_NUMBER = i + 1;
            orderLines[i] = generateOrderLine( order, OL_NUMBER );
        }
        return orderLines;
    }


    /**
     * @param NO_O_ID id of the {@link Order} this {@link NewOrder} should belong to.
     * @param NO_D_ID id of the {@link District} this {@link NewOrder} should belong to.
     * @param NO_W_ID id of the {@link Warehouse} this {@link NewOrder} should belong to.
     */
    public static NewOrder generateNewOrder( final int NO_O_ID, final int NO_D_ID,
            final int NO_W_ID ) {
        return new NewOrder( NO_O_ID, NO_D_ID, NO_W_ID );
    }


    /**
     * Generates {@link NewOrder} objects for the last 900 rows of the required initial {@link Order} objects which are generated per {@link District}.
     *
     * @param district {@link District} for which the {@link NewOrder} objects should be generated.
     */
    public static NewOrder[] generateNewOrdersForDistrict( final District district ) {
        NewOrder[] newOrders = new NewOrder[900];
        for ( int i = 0; i < newOrders.length; i++ ) {
            int NO_O_ID = 2101 + i;
            int NO_D_ID = district.getD_ID();
            int NO_W_ID = district.getD_W_ID();
            newOrders[i] = generateNewOrder( NO_O_ID, NO_D_ID, NO_W_ID );
        }
        return newOrders;
    }


    /**
     * Generates a random a-string which contains ORIGINAL or not.
     *
     * @param lb lb of string length (inclusive)
     * @param ub ub of string length (inclusive)
     * @param containsOriginal whether the string should contain 'ORIGINAL' or not.
     */
    private static String generateOriginalAString( int lb, int ub, boolean containsOriginal ) {
        StringBuilder _return = new StringBuilder( generateAString( lb, ub ) );
        while ( _return.toString().contains( "ORIGINAL" ) ) {
            _return.replace( _return.indexOf( "ORIGINAL" ), _return.indexOf( "ORIGINAL" ) + 8,
                    generateAString( 8, 8 ) );
        }
        if ( containsOriginal ) {  //Fullfills the 10% Requirement
            int start = generateUniform( 0, _return.length() - 8 );    //startposition of 'ORIGINAL'
            _return.replace( start, start + 8, "ORIGINAL" );
        }
        return _return.toString();
    }

}