package org.example;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// hayvanların üretim sınıfı
public class AnimalFactory {

    // dışardan alınan veriler neticesinde hayvan oluşturma, isteğe bağlı olarak bir türün yarısının dişi yarısını erkek olmasını sağlayan kurallar içerir.
    public static List<Animal> generateAnimals(int size, Class<? extends Animal> animalClass, boolean isEqualGender) throws IllegalArgumentException {

        if (animalClass == null || !Animal.class.isAssignableFrom(animalClass)) {
            throw new IllegalArgumentException("Animalın alt sınıfı olmalı");
        }

        List<Animal> animals = new ArrayList<>(size); // istenen boyutta liste oluşturulur

        if (!isEqualGender) { // yarı yarıya cinsiyet ayrımı yapılmıyorsa bu kural çalışır.
            for (int i = 0; i < size; i++) {
                Random random = new Random();
                if(Cow.class.isAssignableFrom(animalClass)){ // İnekler sadece dişi hayvan olduğu için bu koşul konur.
                    animals.add(createAnimal(animalClass,true));
                }
                else{
                    animals.add(createAnimal(animalClass, random.nextBoolean())); // inek dışındaki varlıklara random cinsiyet atanır.
                }


            }
        } else { // yarı yarıya dişi erkek ayrımı yapılır
            if (size % 2 != 0) {
                throw new IllegalArgumentException("boyut çift sayı olmalı ayrım için");
            }

            for (int i = 0; i < size / 2; i++) {

                animals.add(createAnimal(animalClass, true));
                animals.add(createAnimal(animalClass, false));
            }
        }

        return animals;
    }

    // hayvan nesneleri üreten method.
    public static Animal createAnimal(Class<? extends Animal> animalClass, boolean isFemale) {
        try {
            // kuş sınıfından method üretilecekse dişi ve erkek olup olmadınığına bakılır dişi ise tavuk erkek ise horoz olur.
            if (isFemale && Bird.class.isAssignableFrom(animalClass)) {
                return new Chicken();
            }

            else if (!isFemale && Bird.class.isAssignableFrom(animalClass)) {
                return new Rooster();
            }
            else {
                // inekler dişi olduğu için burada istisnai bir koşul var.
                if(isFemale && Cow.class.isAssignableFrom(animalClass)){
                    Constructor<? extends Animal> constructor_1 = animalClass.getConstructor();
                    return constructor_1.newInstance();
                }
                else{
                    Constructor<? extends Animal> constructor = animalClass.getConstructor(boolean.class);
                    return constructor.newInstance(isFemale);
                }

            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Hayvan örneği oluşturulurken hata oluştu ", e);
        }
    }
}
