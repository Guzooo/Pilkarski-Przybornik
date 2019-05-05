package pl.Guzooo.PilkarskiPrzybornik;

public class Games {

    public static final int[] names = new int[] {R.string.game_king};
    public static final int[] descriptions = new int[] {R.string.game_king_description};
    public static final int[] images = new int[] {R.drawable.crown};

    public static int getName(int id){
        return names[id-1];
    }

    public static int getDescription(int id){
        return descriptions[id-1];
    }

    public static int getImage(int id){
        return images[id-1];
    }
}
