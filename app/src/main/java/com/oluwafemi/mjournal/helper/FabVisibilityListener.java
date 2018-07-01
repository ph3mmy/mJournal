package com.oluwafemi.mjournal.helper;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;


public class FabVisibilityListener extends RecyclerView.OnScrollListener{

        private FloatingActionButton fab;
        private Activity mActivity;

        public FabVisibilityListener(FloatingActionButton mFab) {
            this.mActivity = mActivity;
            fab = mFab;
        }


        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (recyclerView.getAdapter().getItemCount() < 4 && !fab.isShown()) {
                fab.show();
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy < -5 && !fab.isShown()) {
                fab.show();
            } else if(dy > 5 && fab.isShown()){
                fab.hide();
            } else if(dy == 1 && !fab.isShown()){
                fab.show();
            }
        }
}
