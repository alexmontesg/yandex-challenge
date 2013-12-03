package nl.tue.we.yandex.reranking14;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.Factorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDPlusPlusFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;

public class YandexRecommender {
	
	private DataModel model;
	private Recommender recommender;
	private static YandexRecommender instance;
	
	public static YandexRecommender create(String filename) throws IOException, TasteException {
		if(instance == null) {
			instance = new YandexRecommender(filename);
		}
		instance.model.refresh(Collections.<Refreshable> emptyList());
		instance.recommender.refresh(Collections.<Refreshable> emptyList());
		return instance;
	}
	
	private YandexRecommender(String filename) throws IOException, TasteException {
		model = new FileDataModel(new File(filename));
		// Recommender just for testing purposes, we have to check which one is better
		Factorizer factorizer = new SVDPlusPlusFactorizer(model, 50, 10);
		recommender = new SVDRecommender(model, factorizer);
	}
	
	public float estimatePreference(long userID, long itemID) throws TasteException {
		return recommender.estimatePreference(userID, itemID);
	}

}
