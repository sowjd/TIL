package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Data Access Object
@Dao
interface ItemDao {
    // DB 작업은 시간이 오래걸릴 수 있으므로 별도의 thread에서 실행해야 한다. (suspend 키워드 사용)
    // Room은 main thread에서 DB 접근을 혀용하지 않음

    // Default는 OnConflictStrategy.ABORT (Rollback)이지만
    // 현재 프로젝트에서는 한 위치에서만 insert를 하기 때문에 IGNORE로 설정함
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    // Flow 타입을 리턴하면 DB의 데이터가 바뀔 때마다 알림을 받는다.
    // Room은 이 Flow 데이터를 자동으로 업데이트한다. 따라서 한번만 데이터를 가져오면 된다.
    // Room은 background thread에서도 쿼리를 실행한다.
    // 그래서 suspend 함수로 만들어서 코루틴 block에서 호출하지 않아도 된다.
    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>
}