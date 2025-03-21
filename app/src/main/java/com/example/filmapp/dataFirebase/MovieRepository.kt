import com.example.filmapp.dataFirebase.Movie
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

object MovieRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun addMovieInMyList(movie: Movie) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .collection("mylist")
            .document(movie.id.toString())
            .set(movie)
            .await()
    }


    suspend fun removeMovieInMyList(movieId: Int) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .collection("mylist")
            .document(movieId.toString())
            .delete()
            .await()
    }

    fun getMyListMovies(): Flow<List<Movie>> = flow {
        val userId = auth.currentUser?.uid ?: return@flow
        val snapshot = db.collection("users").document(userId)
            .collection("mylist").get().await()

        val movies = snapshot.documents.mapNotNull { it.toObject(Movie::class.java) }
        emit(movies)
    }


    suspend fun addFavoritesMovie(movie: Movie) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .collection("favorites")
            .document(movie.id.toString())
            .set(movie)
            .await()
    }


    suspend fun removeFavoritesMovie(movieId: Int) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .collection("favorites")
            .document(movieId.toString())
            .delete()
            .await()
    }

    fun getFavoritesMovies(): Flow<List<Movie>> = flow {
        val userId = auth.currentUser?.uid ?: return@flow
        val snapshot = db.collection("users").document(userId)
            .collection("favorites").get().await()

        val movies = snapshot.documents.mapNotNull { it.toObject(Movie::class.java) }
        emit(movies)
    }
}
