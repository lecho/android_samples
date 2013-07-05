package lecho.sample.green_dao;

import lecho.sample.green_dao.dao.Category;
import lecho.sample.green_dao.dao.CategoryDao;
import lecho.sample.green_dao.dao.DaoMaster;
import lecho.sample.green_dao.dao.DaoMaster.DevOpenHelper;
import lecho.sample.green_dao.dao.DaoSession;
import lecho.sample.green_dao.dao.Faculty;
import lecho.sample.green_dao.dao.FacultyDao;
import lecho.sample.green_dao.dao.Place;
import lecho.sample.green_dao.dao.PlaceDao;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Example of loading one-to-many relation with GreenDao within single
 * AsyncTaskLoader and displaying content on single layout.
 * 
 * - no cascade operations support:(; - no toString, hashCode, equals generated
 * by generator; - no eager loading for one-to-many and many-to-many; - no many-to-many relations support!!!
 * 
 * @author Leszek
 * 
 */
public class MainActivity extends FragmentActivity implements
		LoaderCallbacks<Place> {
	public static final String DB_NAME = "green_dao_database";
	private static final String TAG = MainActivity.class.getSimpleName();
	private long mPlaceId;
	private LinearLayout mMainLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mMainLayout = (LinearLayout) findViewById(R.id.main_layout);
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(
				getApplicationContext(), DB_NAME, null);
		DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
		DaoSession daoSession = daoMaster.newSession();

		// Entities to insert
		Place place = new Place(null, "place1", "B1", "place1 desc");
		PlaceDao placeDao = daoSession.getPlaceDao();
		mPlaceId = placeDao.insert(place);

		Category cat1 = new Category(null, "category1", "category1 desc",
				mPlaceId);
		Category cat2 = new Category(null, "category2", "category2 desc",
				mPlaceId);
		CategoryDao categoryDao = daoSession.getCategoryDao();
		categoryDao.insert(cat1);
		categoryDao.insert(cat2);

		Faculty f1 = new Faculty(null, "faculty1", "faculty1 desc", mPlaceId);
		Faculty f2 = new Faculty(null, "faculty2", "faculty2 desc", mPlaceId);
		FacultyDao facultyDao = daoSession.getFacultyDao();
		facultyDao.insert(f1);
		facultyDao.insert(f2);

		getLoaderManager().initLoader(1, null, this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public Loader<Place> onCreateLoader(int id, Bundle args) {
		return new PlaceLoader(getApplicationContext(), mPlaceId);
	}

	@Override
	public void onLoadFinished(Loader<Place> loader, Place data) {
		// dirty layout generation
		Log.d(TAG, data.toString());
		TextView name = new TextView(this);
		name.setText(data.getName());
		TextView code = new TextView(this);
		code.setText(data.getCode());
		TextView desc = new TextView(this);
		desc.setText(data.getDescription());
		mMainLayout.addView(name);
		mMainLayout.addView(code);
		mMainLayout.addView(desc);

		for (Faculty f : data.getFacultyList()) {
			TextView tv = new TextView(this);
			tv.setText(f.getName() + " " + f.getDescription());
			mMainLayout.addView(tv);
		}

		for (Category c : data.getCategoryList()) {
			TextView tv = new TextView(this);
			tv.setText(c.getName() + " " + c.getDescription());
			mMainLayout.addView(tv);
		}

	}

	@Override
	public void onLoaderReset(Loader<Place> loader) {
		mMainLayout.removeAllViews();

	}

	/**
	 * Warning very basic implementation.
	 * 
	 * @author Leszek
	 * 
	 */
	private static class PlaceLoader extends AsyncTaskLoader<Place> {

		private long mPlaceId;
		private Place mPlace;

		public PlaceLoader(Context context, long placeId) {
			super(context);
			mPlaceId = placeId;
		}

		@Override
		public Place loadInBackground() {
			Log.d(TAG, "loading");
			// second instance of helper(move to singleton?)
			DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(),
					DB_NAME, null);
			DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
			DaoSession daoSession = daoMaster.newSession();
			PlaceDao placeDao = daoSession.getPlaceDao();
			mPlace = placeDao.load(mPlaceId);
			return mPlace;
		}

		@Override
		protected void onStartLoading() {
			if (null != mPlace) {
				deliverResult(mPlace);
			} else {
				forceLoad();
			}
		}

		@Override
		protected void onStopLoading() {
			// Attempt to cancel the current load task if possible.
			cancelLoad();
		}

		@Override
		public void onCanceled(Place apps) {
			super.onCanceled(apps);
		}

		@Override
		protected void onReset() {
			super.onReset();

			// Ensure the loader is stopped
			onStopLoading();

			mPlace = null;
		}

	}

}
