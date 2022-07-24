package br.com.franciscoangelo.specific;

import br.com.franciscoangelo.avro.CustomerAvroV1;
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
        CustomerAvroV1.Builder customerBuilder = CustomerAvroV1.newBuilder();
        customerBuilder.setAge(30);
        customerBuilder.setFirstName("Mark");
        customerBuilder.setLastName("Simpson");
        customerBuilder.setAutomatedEmail(true);
        customerBuilder.setHeight(180f);
        customerBuilder.setWeight(90f);

        CustomerAvroV1 customer = customerBuilder.build();
        System.out.println(customer.toString());

        // step 2: write to a file
        final DatumWriter<CustomerAvroV1> datumWriter = new SpecificDatumWriter<>(CustomerAvroV1.class);
        try (DataFileWriter<CustomerAvroV1> dataFileWriter = new DataFileWriter<>(datumWriter)) {
            dataFileWriter.create(customer.getSchema(), new File("customer-specific.avro"));
            dataFileWriter.append(customer);
            System.out.println("successfully wrote customer-specific.avro");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // step 3: read from a file
        final File file = new File("customer-specific.avro");
        final DatumReader<CustomerAvroV1> datumReader = new SpecificDatumReader<>(CustomerAvroV1.class);
        final DataFileReader<CustomerAvroV1> dataFileReader;
        try {
            System.out.println("Reading our specific record");
            dataFileReader = new DataFileReader<>(file, datumReader);
            while (dataFileReader.hasNext()) {
                CustomerAvroV1 readCustomer = dataFileReader.next();
                System.out.println(readCustomer.toString());
                System.out.println("First name: " + readCustomer.getFirstName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}