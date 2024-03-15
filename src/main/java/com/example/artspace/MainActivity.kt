package com.example.artspace

import androidx.compose.foundation.Image
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.layout.ContentScale
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.artspace.data.DataSource
import com.example.artspace.ui.theme.ArtSpaceTheme
import androidx.compose.ui.text.style.TextAlign


class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val navController = rememberNavController()
      ArtSpaceTheme {
        NavHost(
          navController = navController, startDestination = Screen.Home.route + "/{id}"
        ) {
          composable(
            Screen.Home.route + "/{id}", arguments = listOf(navArgument("id") {
              type = NavType.IntType
              defaultValue = 0
            })
          ) {
            HomePage(navController)
          }
          composable(
            Screen.Artist.route + "/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
          ) {
            ArtistPage(navController)
          }
        }
      }
    }
  }
}

@Composable
fun ArtistPage(navController: NavController) {
  val id = navController.currentBackStackEntry?.arguments?.getInt("id") ?: 0
  val art = DataSource.arts.getOrNull(id)
  val scrollState = rememberScrollState()

  Box(modifier = Modifier.fillMaxSize()) {
    art?.let {
      Column(
        modifier = Modifier
          .padding(16.dp)
          .verticalScroll(scrollState)
          .align(Alignment.TopStart)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
        ) {
          Image(
            painter = painterResource(id = it.artistImageId),
            contentDescription = stringResource(id = it.artistId),
            modifier = Modifier
              .size(128.dp)
              .clip(CircleShape)
          )
          Column(
            modifier = Modifier
              .padding(start = 16.dp)
              .align(Alignment.CenterVertically)
          ) {
            Text(
              text = stringResource(id = it.artistId),
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp
            )
            Text(
              text = stringResource(id = it.artistInfoId),
              fontSize = 16.sp
            )
          }
        }

        Text(
          text = stringResource(id = it.artistBioId),
          style = MaterialTheme.typography.bodyMedium,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
        )
      }
    } ?: Text(
      text = "Artist not found",
      modifier = Modifier.padding(16.dp)
    )

    Button(
      onClick = { navController.navigateUp() },
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(16.dp)
    ) {
      Text(text = stringResource(id = R.string.back))
    }
  }
}


@Composable
fun ArtWall(
  artistId: Int,
  artImageId: Int,
  titleId: Int,
  yearId: Int,
  currentArtIndex: Int,
  onNavigateNext: () -> Unit,
  onNavigatePrevious: () -> Unit,
  navController: NavController
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
      verticalArrangement = Arrangement.SpaceBetween
  ) {
    Image(
      painter = painterResource(id = artImageId),
      contentDescription = stringResource(id = titleId),
      modifier = Modifier
        .clickable {
          navController.navigate("artist/$currentArtIndex")
        }
        .fillMaxWidth()
        .height(300.dp),
      contentScale = ContentScale.Crop
    )

    Spacer(modifier = Modifier.height(205.dp))

    Text(
      text = stringResource(id = titleId),
      fontWeight = FontWeight.Bold,
      fontSize = 20.sp,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
    )

    Text(
      text = "${stringResource(id = artistId)} ${stringResource(id = yearId)}",
      fontSize = 18.sp,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Button(
        onClick = { onNavigatePrevious() },
        enabled = currentArtIndex > 0,
        modifier = Modifier
          .weight(1f)
      ) {
        Text(text = "Previous")
      }

      Spacer(modifier = Modifier.width(8.dp))

      Button(
        onClick = { onNavigateNext() },
        enabled = currentArtIndex < DataSource.arts.size - 1,
        modifier = Modifier
          .weight(1f)
      ) {
        Text(text = "Next")
      }
    }
  }
}














@Composable
fun ArtDescriptor(artTitleId: Int, artistId: Int, artYearId: Int) {
  Text(
    text = "Title: ${stringResource(id = artTitleId)}\nArtist: ${stringResource(id = artistId)}\nYear: ${stringResource(id = artYearId)}",
    fontSize = 16.sp,
    modifier = Modifier.padding(16.dp)
  )
}

@Composable
fun DisplayController(current: Int, move: (Int) -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Button(
      onClick = {
        if (current > 0) {
          move(current - 1)
        }
      },
      enabled = current > 0,
      modifier = Modifier
        .weight(1f)
        .height(48.dp)
    ) {
      Text(text = "Previous")
    }

    Spacer(modifier = Modifier.width(16.dp))

    Button(
      onClick = {
        if (current < DataSource.arts.size - 1) {
          move(current + 1)
        } else {
          move(0)
        }
      },
      enabled = current < DataSource.arts.size - 1,
      modifier = Modifier
        .weight(1f)
        .height(48.dp)
    ) {
      Text(text = "Next")
    }
  }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {
  var currentArtIndex by remember { mutableStateOf(0) }
  val arts = DataSource.arts

  Scaffold(topBar = {
    CenterAlignedTopAppBar(
      title = { Text(text = stringResource(id = R.string.app_name)) },
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary
      )
    )
  }) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      val art = arts.getOrNull(currentArtIndex)

      art?.let {
        ArtWall(
          artistId = it.artistId,
          artImageId = it.artworkImageId,
          titleId = it.titleId,
          yearId = it.yearId,
          currentArtIndex = currentArtIndex,
          onNavigateNext = {
            currentArtIndex = (currentArtIndex + 1) % arts.size
          },
          onNavigatePrevious = {
            currentArtIndex = (currentArtIndex - 1).coerceAtLeast(0)
          },
          navController = navController
        )

        ArtDescriptor(it.titleId, it.artistId, it.yearId)


        DisplayController(currentArtIndex) { newIndex ->
          currentArtIndex = newIndex
        }
      }
    }
  }
}



@Preview(showBackground = true)
@Composable
fun ArtSpaceAppPreview() {
  ArtSpaceTheme {
    HomePage(rememberNavController())
  }
}
