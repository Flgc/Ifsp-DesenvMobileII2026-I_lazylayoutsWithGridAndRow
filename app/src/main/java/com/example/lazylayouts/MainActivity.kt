package com.example.lazylayouts

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.lazylayouts.ui.theme.LazyLayoutsTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LazyLayoutsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListOfOptions(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class product(val id: Int, val name: String, val price: Double, val image: String)

data class productState(val products: List<product> = emptyList())

class productViewModel : ViewModel() {
    private val _state = MutableStateFlow(productState())
    val state: StateFlow<productState> = _state

    init {
        generateproducts()
    }

    private fun generateproducts() {
        val productslist = List(60) {
            product(
                id = it,
                name = "Product $it",
                price = (10..100).random().toDouble(),
                image = "https://picsum.photos/200?random=$it"
            )
        }
        _state.value = productState(products = productslist)
    }
}

@Composable
fun productCard(product: product, onCLick: (product) -> Unit) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {onCLick(product)}
    ) {
        Row() {
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(60.dp))
            )
            Column() {
                Text(
                    "Name: ${product.name}",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                Text(
                    "Value: ${product.price}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
            }
        }

    }
}

@Composable
fun SimpleList(modifier: Modifier = Modifier, viewModel: productViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LazyColumn() {
        items(
            state.products,
            key = { product -> product.id }
        )
        { product ->
            productCard(product) {
                Toast.makeText(
                    context,
                    "Clicked ${product.name}",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(60) { index -> Text("Item $index") }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LazyLayoutsTheme {
        Greeting("Android")
    }
}

@Composable
fun RowList(modifier: Modifier = Modifier, viewModel: productViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Products (Horizontal Scroll)",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                state.products,
                key = { product -> product.id }
            ) { product ->
                horizontalProductCard(product) {
                    Toast.makeText(
                        context,
                        "Clicked ${product.name}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

@Composable
fun horizontalProductCard(product: product, onClick: (product) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(width = 160.dp, height = 200.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { onClick(product) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Text(
                "Name: ${product.name}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(text = "R$ ${String.format("%.2f", product.price)}")
        }
    }
}

@Composable
fun GridList(modifier: Modifier = Modifier, viewModel: productViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Products (Grid Layout)",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Two columns, can be changed to 3
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                state.products,
                key = { product -> product.id }
            ) { product ->
                ProductCardGrid(product) {
                    Toast.makeText(
                        context,
                        "Clicked ${product.name}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

@Composable
fun ProductCardGrid(product: product, onClick: (product) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { onClick(product) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Text(
                "Name: ${product.name}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "R$ ${String.format("%.2f", product.price)}",
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

enum class LayoutType {
    LIST,       // LazyColumn
    ROW,         // LazyRow
    GRID         // LazyVerticalGrid
}

@Composable
fun ListOfOptions(modifier: Modifier = Modifier) {
    var layoutType by remember { mutableStateOf(LayoutType.LIST) }

    Column(modifier = modifier.fillMaxSize()) {
        // Buttons to switch layouts
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { layoutType = LayoutType.LIST }) {
                Text("List")
            }
            Button(onClick = { layoutType = LayoutType.ROW }) {
                Text("Horizontal")
            }
            Button(onClick = { layoutType = LayoutType.GRID }) {
                Text("Grid")
            }
        }

        // Displays the selected layout
        when (layoutType) {
            LayoutType.LIST -> SimpleList(modifier = Modifier.weight(1f))
            LayoutType.ROW -> RowList(modifier = Modifier.weight(1f))
            LayoutType.GRID -> GridList(modifier = Modifier.weight(1f))
        }
    }
}