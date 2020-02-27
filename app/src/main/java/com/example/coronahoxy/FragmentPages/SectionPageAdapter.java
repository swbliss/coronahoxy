package com.example.coronahoxy.FragmentPages;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionPageAdapter extends FragmentPagerAdapter {
	private final List<Fragment> mFragmentList = new ArrayList<>();

	public SectionPageAdapter(@NonNull FragmentManager fm, int behavior) {
		super(fm, behavior);
	}

	public SectionPageAdapter(FragmentManager supportFragmentManager) {
		super(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
	}

	public void addFragment(Fragment fragment){
		mFragmentList.add(fragment);
	}

	@NonNull
	@Override
	public Fragment getItem(int position) {
		return mFragmentList.get(position);
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
	}
}
