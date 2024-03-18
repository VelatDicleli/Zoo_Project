package org.example;

import java.awt.event.TextEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// İş kurallarımızın gerçekleştiği kısım
public class Zoo<T extends Animal> {

    // hayvanlar listesi tutulur.
    public List<T> animals = initializeAnimals();

    // yapıcı method içinde simülasyon başlatılır ve en son toplam hayvan sayısı yazılır.
    public Zoo() {
        run();
        TotalAnimal();
    }

    // hayvanlar tek tek oluşturulur.
    private List<T> initializeAnimals() {
        List<Animal> wolves = AnimalFactory.generateAnimals(10, Wolf.class, true);
        List<Animal> sheeps = AnimalFactory.generateAnimals(30, Sheep.class, true);
        List<Animal> lions = AnimalFactory.generateAnimals(10, Lion.class, true);
        List<Animal> cows = AnimalFactory.generateAnimals(10, Cow.class, false);
        List<Animal> birds = AnimalFactory.generateAnimals(20, Bird.class, true);
        List<Animal> huntsman = AnimalFactory.generateAnimals(1, Huntsman.class, false);



        // oluşturulan hayvan listeleri tek bir listede birleştirilir.
        var animals = (List<T>) Stream.of(wolves, sheeps, lions,cows, birds,huntsman)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return animals;
    }


    // olayları gerçekleştirme methodu.
    private void run(){
        // her türdeki hayvan ( avcı da dahil ) aldığı toplam 1000 birim mesafe 100 döngüde tamamlanır. )

        for(int i = 0;i<100;i++){

            huntOtherAnimal();
            populateAnimal();

        }


    }

    // toplam hayvan sayısını gösteren method ( avcı'nın olmadığı sayıyı ekrana bastırır o yüzden size - 1 ).
    public void TotalAnimal(){

        System.out.print(animals.size()-1);


    }

    // hayvanların hareket etmesini sağlayan method.
    private void moveAllAnimals() {
        for (Animal animal : animals) {
            animal.moveAnyWhere();
        }
    }

    // bir listenin hem sağına hem de soluna göre arama yaparak avlama yapan method.
    private void huntOtherAnimal() {

        for (int i = 0; i < animals.size(); i++) {
            var animal = animals.get(i);
            if (!(animal instanceof Wolf || animal instanceof Lion || animal instanceof Huntsman)) {
                continue;
            }

            for (int j = i + 1; j < animals.size(); j++) {
                var nextAnimal = animals.get(j);
                doHunt(animal,nextAnimal);
            }

            for (int k = i - 1; k >= 0; k--) {
                var previousAnimal = animals.get(k);
                doHunt(animal,previousAnimal);
            }

            moveAllAnimals(); // Avlandıktan - avladıktan sonra hareket etsinler.
        }


    }

    // hayvanların birbiri arasındaki avlama kuralına göre olan method.
    private void doHunt(T hunter,T preyer){
        if(hunter instanceof Wolf && (preyer instanceof Sheep || preyer instanceof Chicken || preyer instanceof Rooster)){
            double distance = calculateDistance(hunter,preyer);
            if(distance<=4){
                animals.remove(preyer);
                System.out.println("Hayvan avlandı");

            }

        }
        else if(hunter instanceof Lion && (preyer instanceof Cow || preyer instanceof Sheep)){
            double distance = calculateDistance(hunter,preyer);
            if(distance<=5){
                animals.remove(preyer);
                System.out.println("Hayvan avlandı");

            }
        }
        else if(hunter instanceof Huntsman && !(preyer instanceof Huntsman)){
            double distance = calculateDistance(hunter,preyer);
            if(distance<=8){
                animals.remove(preyer);
                System.out.println("Hayvan avlandı");

            }
        }

    }

    // hayvanların mesafelerine, türüne ve cinsiyetine göre üreme yapan method.
    private void populateAnimal(){

        for (int i = 0; i < animals.size()-1; i++) {
            T animal1 = animals.get(i);

            for (int j = i + 1; j < animals.size(); j++) {
                T otherAnimal = animals.get(j);
                double distance = calculateDistance(animal1, otherAnimal);


                if (distance>0 && distance<=3 && animal1.getClass().equals(otherAnimal.getClass()) && !(animal1.getGender()==otherAnimal.getGender())) {
                    Random rand = new Random();
                    T newAnimal = (T) AnimalFactory.createAnimal(animal1.getClass(), rand.nextBoolean());
                    animals.add(newAnimal);
                    System.out.println("Yeni hayvan oluşturuldu: " + newAnimal.getX() + "," + newAnimal.getY() + "|" + newAnimal.getGender());
                    moveAllAnimals(); 
                    // bir çiftin yeni aile üyesi oluştuktan sonra hayvanlar hareket etmeli
                    // eğer hareket etmez ise ilgili çift sürekli aynı yerde kaldığı için aynı şekilde üremeye devam eder.
                    // Bir eylemden sonra hayvanlar hareket etmeli.
                }

            }
        }
    }


    // hayvanlar arasındaki mesafeyi ölçen iki nokta arasındaki fark formülü.
    private double calculateDistance(T object1, T object2) {
        return Math.sqrt(Math.pow(object1.getX() - object2.getX(), 2) + Math.pow(object1.getY() - object2.getY(), 2));
    }
}
