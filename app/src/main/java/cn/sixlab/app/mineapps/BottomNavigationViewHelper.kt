//package cn.sixlab.app.mineapps
//
//import android.annotation.SuppressLint
//import android.support.design.internal.BottomNavigationItemView
//import android.support.design.internal.BottomNavigationMenuView
//import android.support.design.widget.BottomNavigationView
//
///**
// * Created by patrick on 2017/12/27.
// */
//object BottomNavigationViewHelper {
//    @SuppressLint("RestrictedApi")
//    fun formatDate(navigation: BottomNavigationView) {
//        val menuView = navigation.getChildAt(0) as BottomNavigationMenuView
//        try {
//            val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
//
//            shiftingMode.isAccessible = true
//            shiftingMode.setBoolean(menuView, false)
//            shiftingMode.isAccessible = false
//
//            for (i in 0..menuView.childCount - 1) {
//                val itemView = menuView.getChildAt(i) as BottomNavigationItemView
//                itemView.setShiftingMode(false)
//                itemView.setChecked(itemView.itemData.isChecked)
//            }
//
//        } catch (e: Exception) {//NoSuchFieldException | IllegalAccessException e
//            e.printStackTrace()
//        }
//    }
//}