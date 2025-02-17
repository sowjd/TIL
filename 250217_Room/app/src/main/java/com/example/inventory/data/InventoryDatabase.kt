package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Database instance 생성: Entity와 DAO를 사용

// entities: list of entities (여기서는 Item class만 설정)
// version: DB table 스키마를 변경할 때마다 버전 번호를 높여야 한다.
// exportSchema: 스키마 기록을 백업할거냐?
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object { // DB를 만들거나 가져옴
        @Volatile // 값이 캐시되지 않고 모든 읽기와 쓰기는 main 메모리에서 진행됨, 항상 최신값 유지, 모든 thread에서 동일한 값 유지
        private var Instance: InventoryDatabase? = null // DB 참조

        fun getDatabase(context: Context): InventoryDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) { // synchronized: 하나의 thread만 실행
                Room.databaseBuilder(context, InventoryDatabase::class.java, "item_database")
                    .build()
                    .also { Instance = it } // Instance가 null이었어서 synchronized 블록이 실행된 결과 it을 Instance에 저장
            }
        }
    }
}