package uk.co.brett.surviving.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import uk.co.brett.surviving.io.file.FileHashes;
import uk.co.brett.surviving.io.repo.InputHashRepo;

@SpringBootTest//(classes = UnitTestConfig.class)
class InputExistsTest {

    @Autowired
    ConfigurableApplicationContext applicationContext;

    @Autowired
    IngestService ingestService;
    @Autowired
    InputHashRepo hashRepo;
    @Autowired
    private FileHashes fileHashes;


//    @Test
//    void test() throws IOException {
//
//        Map<GameVariant, InputFile> map = fileHashes.getMap();
//
//        for (GameVariant variant : GameVariant.values()) {
//
//            String s = map.get(variant).getResourceLocation();
//            String originalString = IOUtils.resourceToString(s, StandardCharsets.UTF_8);
//
//            String sha256hex = Hashing.sha256()
//                    .hashString(originalString, StandardCharsets.UTF_8)
//                    .toString();
//
//            System.out.println(variant);
//            System.out.println("Pre-hashed:  " + map.get(variant).getHash());
//            System.out.println("Post-hashed: " + sha256hex);
//
//            System.out.println();
//        }
//
//    }


//    @Test
//    void populateIngestTable() {
//        Map<GameVariant, InputFile> map = fileHashes.getMap();
//
//        for (GameVariant variant : GameVariant.values()) {
//
//            String filename = map.get(variant).getResourceLocation();
//            String hash = map.get(variant).getHash();
//            InputHash inputHash = new InputHash(variant, filename, hash);
//            hashRepo.save(inputHash);
//        }
//
//        hashRepo.flush();
//
//    }
}

