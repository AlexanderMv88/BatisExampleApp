package org.BatisExampleApp;

import org.BatisExampleApp.entity.Owner;
import org.BatisExampleApp.entity.Pet;
import org.BatisExampleApp.mapper.OwnerMapper;
import org.BatisExampleApp.mapper.PetMapper;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BatisExampleAppApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BatisOwnerTests {

	@Autowired
	PetMapper petMapper;

	@Autowired
	OwnerMapper ownerMapper;



	@Test
	public void test03_fillTableAndFindAll() throws InterruptedException {
		List<Owner> owners = Arrays.asList(new Owner("Вася"), new Owner("Коля"), new Owner("Петя"), new Owner("Марина"));
		for (Owner owner:owners) {
			ownerMapper.create(owner);
		}



		System.out.println("owners = "+owners);
		Assert.assertEquals(4, ownerMapper.findAll().size());
	}

	@Test
	public void test04_createOwnerWithPetsAndFindAll() throws InterruptedException {
		List<Owner> ownersWithPets = Arrays.asList(new Owner("Алексей").addPet(new Pet("Ася")).addPet(new Pet("Мотя")));

		for (Owner owner:ownersWithPets) {
			ownerMapper.create(owner);
			Owner savedOwner = ownerMapper.findOneByName("Алексей");
			System.out.println("savedOwner = " + savedOwner);
			for (Pet pet:owner.getPets()) {
				pet.setOwner_id(savedOwner.getId());
				petMapper.create(pet);
				List<Pet> savedPets = petMapper.findAll();
				System.out.println("savedPets = " + savedPets);
			}
		}

		List<Owner> ownersWithPetsFromDB = ownerMapper.findAllWithPets();
		Assert.assertEquals(5,ownersWithPetsFromDB.size());
		System.out.println("ownersWithPetsFromDB = " + ownersWithPetsFromDB);
		Owner owner = ownersWithPetsFromDB.get(4);



		Pet pet1 = owner.getPets().get(0);
		Pet pet2 = owner.getPets().get(1);

		Assert.assertEquals("Ася",pet1.getName());
		Assert.assertEquals("Мотя",pet2.getName());
	}


	@Test
	public void test05_findAndUpdate(){
		String name = "Вася";
		Owner owner = ownerMapper.findOneByName(name);
		Assert.assertEquals(name, owner.getName());

		String newName = "Василий";
		owner.setName(newName);
		ownerMapper.save(owner);
		Owner updatedOwner = ownerMapper.findOneByName(newName);
		Assert.assertEquals(newName, updatedOwner.getName());
	}


	@Test
	public void test06_removeAndCount(){
		String name = "Василий";
		Owner owner = ownerMapper.findOneByName(name);
		Assert.assertEquals(name, owner.getName());

		ownerMapper.delete(owner);

		Assert.assertEquals(4, ownerMapper.count().intValue());

	}

	@Test
	public void test07_removeListOfRecords(){
		List<Owner> owners = ownerMapper.findByNames("Петя", "Марина");
		Assert.assertEquals(2, owners.size());
		Assert.assertTrue(owners.stream().anyMatch(owner -> owner.getName().equals("Петя")));
		Assert.assertTrue(owners.stream().anyMatch(owner -> owner.getName().equals("Марина")));

		ownerMapper.deleteAllWhereIdIn(owners.stream().map(Owner::getId).collect(Collectors.toList()));
		Assert.assertEquals(2, ownerMapper.count().intValue());

	}




}
