import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

public class RestaurantApp {

	private Random m_random;
	private Restaurant m_restaurant;
	private boolean m_clientHasChildren;
	private ArrayList<Integer> m_selectedTablesId;
	protected HashMap<Integer, Dish> m_orderedDishes;

	public RestaurantApp() {
		this.m_random = new Random();
		this.m_selectedTablesId = new ArrayList<>();
		this.m_orderedDishes = new HashMap<>();
	}

	public void simulate() {
		if (indicateChildPresence()
			&& selectRestaurant()
			&& choseTable()
			&& callWaiter()
			&& getMenu()
			&& makeOrders())
			{
				try {
					TimeUnit.SECONDS.sleep(10);
					checkOut();
				} catch (Exception exception) { }
			}
		else { return; }
	}

	protected boolean indicateChildPresence() {
		m_clientHasChildren = m_random.nextBoolean();
		if (m_clientHasChildren)
			System.out.println("Client has children");
		else
			System.out.println("Client doesn't have children");
		return true;
	}

	protected boolean selectRestaurant() {
		final int nrOfRestaurants = 2;
		final int chosenRestaurant = m_random.nextInt(nrOfRestaurants);

		if (chosenRestaurant == 0) { // italian restaurant
			m_restaurant = new ItalianRestaurant();
			System.out.println("Italian restaurant was chosen");
		} else { // french restaurant
			if (m_clientHasChildren)
			{
				System.out.println("French restaurant does not have child tables");
				return false;
			}
			m_restaurant = new FrenchRestaurant();
			System.out.println("French restaurant was chosen");
		}

		return true;
	}

	protected boolean choseTable() {
		if (m_restaurant.getNrOfFreeTables() == 0) {
			System.out.println("No tables available");
			return false;
		}

		if (m_clientHasChildren)
		{
			final int childTableId = 201;
			if (m_restaurant.occupyTable(childTableId)) {
				System.out.println("Child table occupied");
				m_selectedTablesId.add(childTableId);
			} else {
				System.out.println("No free child tables");
				return false;
			}
		}

		for (Map.Entry<Integer, Table> table
			: m_restaurant.getFreeTable().entrySet())
		{
			if (m_restaurant.occupyTable(table.getKey()))
			{
				m_selectedTablesId.add(table.getKey());
				System.out.println("Table occupied");
				return true;
			}
		}
		System.out.println("No free tables found");
		return false;
	}

	protected boolean callWaiter() {
		System.out.println("Waiter came");
		return true;
	}

	protected boolean getMenu() {
		// System.out.println("Menu");
		// m_restaurant.getMenu().displayDishes();
		// System.out.println("");
		return true;
	}


	protected boolean makeOrders() {
		HashMap<Integer, Dish> dishes = m_restaurant.getMenu().getDishes();

		if (!m_clientHasChildren) {
			if (m_restaurant instanceof FrenchRestaurant) {
				final int sparklingWine = 151;
				m_orderedDishes.put(sparklingWine, dishes.get(sparklingWine));
			}
		} else {
			final int waterId = 101;
			m_orderedDishes.put(waterId, dishes.get(waterId));
		}

		final int maxNrOfOrders = 4;
		final int nrOfOrders = m_random.nextInt(maxNrOfOrders);
		for (int i = 0; i <= nrOfOrders; i++) {
			final int productId = m_random.nextInt(6);
			if (dishes.containsKey(productId))
			{
				final Dish dish = dishes.get(productId);
				m_orderedDishes.put(productId, dish);
				System.out.println(dish.Name + " ordered successfully");
			}
		}

		return true;
	}

	protected boolean checkOut() {
		float sumToPay = 0;

		for (Map.Entry<Integer, Dish> dish: m_orderedDishes.entrySet()) {
			sumToPay += dish.getValue().Price;
		}

		System.out.print("Price to pay: $");
		System.out.println(sumToPay);
		return true;
	}
}
