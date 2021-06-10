package com.adesso.digitalwash.services;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.adesso.digitalwash.model.Category;
import com.adesso.digitalwash.model.Cloth;
import com.adesso.digitalwash.model.Laundry;
import com.adesso.digitalwash.model.PdfFactory;

@Service
public class PdfService {

	public byte[] createMailPdf(Laundry laundry, Collection<Category> categories) {
		String headlines[] = { "Kategorie", "Stückzahl", "Preis" };
		ArrayList<String[]> values = getMailValues(laundry.getClothes(), categories);
		LocalDate date = laundry.getDeliveryDate();
		List<Category> filterCategories = getFilteredCategories(categories);
		String owner = laundry.getLaundryOwner();

		ByteArrayOutputStream baos = new PdfFactory().createPdf(headlines, values, date, filterCategories, owner, false,
				true);

		return baos.toByteArray();
	}

	public byte[] createExportShirtsPdf(ArrayList<Laundry> laundries, Collection<Category> categories) {
		Category shirtCategory = (Category) categories.toArray()[0];
		String headlines[] = { "Name", shirtCategory.getName() };
		ArrayList<String[]> values = getExportShirtsValues(laundries, shirtCategory);
		LocalDate date = laundries.get(0).getAcceptedDate();
		List<Category> filterCategories = getFilteredCategories(categories);

		ByteArrayOutputStream baos = new PdfFactory().createPdf(headlines, values, date, filterCategories, null, true,
				false);

		return baos.toByteArray();
	}

	public byte[] createExportNonShirtsPdf(ArrayList<Laundry> laundries, Collection<Category> categories) {
		Category shirtCategory = (Category) categories.toArray()[0];
		String headlines[] = { "Name", "Kleidungsstück", "Stückzahl" };

		Category categoriesArr[] = categories.toArray(new Category[categories.size()]);
		Collection<Long> filterCatsIds = new ArrayList<Long>();
		for (Category category : categories) {
			if (category.isActive()) {
				filterCatsIds.add(category.getId());
			}
		}

		ArrayList<String[]> values = getExportNonShirtsValues((List<Laundry>) laundries, shirtCategory, categoriesArr, filterCatsIds);
		LocalDate date = laundries.get(0).getAcceptedDate();
		List<Category> filterCategories = getFilteredCategories(categories);

		ByteArrayOutputStream baos = new PdfFactory().createPdf(headlines, values, date, filterCategories, null, false,
				false);

		return baos.toByteArray();
	}

	private ArrayList<String[]> getMailValues(List<Cloth> clothes, Collection<Category> categories) {
		ArrayList<String[]> values = new ArrayList<>();

		float totalCosts = 0;
		for (Cloth cloth : clothes) {
			Integer clothCount = cloth.getClothCount();
			Category clothCategory = new Category();
			for (Category category : categories) {
				if (category.getId() == cloth.getCategoryId()) {
					clothCategory = category;
				}
			}
			if (clothCount != 0 && clothCategory != null) {
				totalCosts += clothCount * clothCategory.getPrice().floatValue();
				values.add(new String[] { clothCategory.getName(), clothCount.toString(),
						String.format(Locale.GERMAN, "%.2f", clothCategory.getPrice().floatValue()) + "€" });
			}
		}
		values.add(new String[] { "Gesamtkosten", "-", String.format(Locale.GERMAN, "%.2f", totalCosts) + "€" });
		return values;
	}

	private ArrayList<String[]> getExportShirtsValues(ArrayList<Laundry> laundries, Category shirtCategory) {
		ArrayList<String[]> values = new ArrayList<>();
		laundries.forEach(l -> {
			if (l.getClothCountOfCategory(shirtCategory.getId()) != 0) {
				values.add(new String[] { l.getLaundryOwner() + "", l.getClothes().stream().filter(c -> {
					return c.getCategoryId().equals(shirtCategory.getId());
				}).findFirst().get().getClothCount() + "" });
			}
		});
		return values;
	}

	private ArrayList<String[]> getExportNonShirtsValues(List<Laundry> laundries, Category shirtCategory,
			Category categoriesArr[], Collection<Long> filterCatsIds) {
		ArrayList<String[]> values = new ArrayList<>();
		laundries.stream().forEach(l -> {
			l.getClothes().forEach(c -> {
				if (c.getCategoryId() != shirtCategory.getId() && c.getClothCount() != 0
						&& filterCatsIds.contains(c.getCategoryId())) {
					values.add(new String[] { l.getLaundryOwner() + "",
							categoriesArr[c.getCategoryId().intValue() - 1].getName(), c.getClothCount() + "" });
				}
			});
		});
		return values;
	}

	private ArrayList<Category> getFilteredCategories(Collection<Category> allCategories) {
		ArrayList<Category> filteredCategories = new ArrayList<>();
		for (Category category : allCategories) {
			if (category.isActive()) {
				filteredCategories.add(category);
			}
		}
		return filteredCategories;
	}
}
