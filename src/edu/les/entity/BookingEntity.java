package edu.les.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity(name = "booking")
@Table(name = "booking")
public class BookingEntity {
	private int bookingId;
	private UserEntity userEntity;
	private RoomEntity roomEntity;
	private Date checkIn;
	private Date checkOut;
	private float finalAmout;
	private String status = "A";

	public BookingEntity() {
		this.checkIn = new Date();
		this.daysPast(this.getCheckIn());
	}

	@Id
	@GeneratedValue
	@Column(name = "booking_id", nullable = false)
	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_cpf")
	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "room_id")
	public RoomEntity getRoomEntity() {
		return roomEntity;
	}

	public void setRoomEntity(RoomEntity roomEntity) {
		this.roomEntity = roomEntity;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name = "check_in", nullable = false)
	public Date getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name = "check_out", nullable = true)
	public Date getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}

	@Transient
	public float getFinalAmout() {
		float pricePerDay = this.roomEntity.getRoomCategory().getPrice();
		int days = this.getDaysPast();
		finalAmout = pricePerDay * days;
		return finalAmout;
	}

	public void setFinalAmout(float finalAmout) {
		this.finalAmout = finalAmout;
	}

	@Column(name = "status", nullable = false, length = 1)
	public String getStatus() {
		return status;
	}

	public void setStatus(String bookingStatus) {
		this.status = bookingStatus;
	}
	
	@Transient
	public int getDaysPast() {
		return this.daysPast(this.getCheckIn());
	}

	private int daysPast(Date checkin) {
		int daysPast = 0;
		try {
			Date today = new Date();
			if (this.getCheckOut() != null) {
				today = this.getCheckOut();
			}
			long diff = today.getTime() - checkin.getTime();
			daysPast = (int) (diff / (1000 * 60 * 60 * 24));
		} catch (NullPointerException e) {
			System.out.println("theres no check in date");
		}
		return daysPast;
	}
}
