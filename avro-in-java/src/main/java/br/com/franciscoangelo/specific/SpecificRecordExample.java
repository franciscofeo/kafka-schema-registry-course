package br.com.franciscoangelo.specific;

import com.example.Customer;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;

public class SpecificRecordExample {

    public static void main(String[] args) {

        // step 1: build specific record
        CustomerAvro.Builder customerBuilder = CustomerAvro.newBuilder();
        customerBuilder.setAge(30);
        customerBuilder.setFirstName("Mark");
        customerBuilder.setLastName("Simpson");
        customerBuilder.setAutomatedEmail(true);
        customerBuilder.setHeight(180f);
        customerBuilder.setWeight(90f);

        CustomerAvro customer = customerBuilder.build();
        System.out.println(customer.toString());

        // step 2: write to a file
        final DatumWriter<CustomerAvro> datumWriter = new SpecificDatumWriter<>(CustomerAvro.class);
        try (DataFileWriter<CustomerAvro> dataFileWriter = new DataFileWriter<>(datumWriter)) {
            dataFileWriter.create(customer.getSchema(), new File("customer-specific.avro"));
            dataFileWriter.append(customer);
            System.out.println("successfully wrote customer-specific.avro");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // step 3: read from a file
        final File file = new File("customer-specific.avro");
        final DatumReader<CustomerAvro> datumReader = new SpecificDatumReader<>(CustomerAvro.class);
        final DataFileReader<CustomerAvro> dataFileReader;
        try {
            System.out.println("Reading our specific record");
            dataFileReader = new DataFileReader<>(file, datumReader);
            while (dataFileReader.hasNext()) {
                CustomerAvro readCustomer = dataFileReader.next();
                System.out.println(readCustomer.toString());
                System.out.println("First name: " + readCustomer.getFirstName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}